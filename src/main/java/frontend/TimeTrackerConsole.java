package frontend;

import domain.TimeTable;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * System for displaying and logging employee time worked
 */
public class TimeTrackerConsole extends SystemConsole {
    private int employeeID;

    private static final HelpDisplay help = HelpDisplay.builder()
            .add("clock", "Clock in or out.")
            .add("time", "Display the current time worked in hours.")
            .build();

    private static final ConsoleSelection clockSelection = ConsoleSelection.builder()
            .add("Check In", "clock_in")
            .add("Check Out", "clock_out")
            .build();

    private static final ConsoleSelection timeViewSelection = ConsoleSelection.builder()
            .add("Today", "today")
            .add("Month", "month")
            .build();

    public TimeTrackerConsole(Scanner scanner) {
        super(scanner);
    }

    @Override
    String getSystemName() {
        return "Time Tracker";
    }

    @Override
    protected void init() {
        System.out.println("Enter an employee ID");
        employeeID = Integer.parseInt(scanner.nextLine());
    }

    @Override
    protected boolean executeCommand(String command) {
        switch (command) {
            case "clock":
                clock();
                return true;
            case "time":
                time();
                return true;
        }

        return false;
    }

    @Override
    protected void displayHelp() {
        help.display();
    }

    /**
     * Log an employee clocking in or out.
     */
    private void clock() {
        String command = clockSelection.select(scanner);
        if (command.equals("clock_in")) {
            if (TimeTable.checkIn(employeeID)) {
                System.out.println("Clocked in.");
            } else {
                System.err.println("Failed to clock in.");
            }
        } else if (command.equals("clock_out")) {
            if (TimeTable.checkOut(employeeID)) {
                System.out.println("Clocked out");
            } else {
                System.err.println("Failed to clock out.");
            }
        }
    }

    /**
     * Display information to the user about how much time they've worked.
     */
    public void time() {
        // how about a today command, that does this stuff.
        String answer = timeViewSelection.select(scanner);

        if (answer.equals("today")) {
            displayTodayTime();
        } else if (answer.equals("month")) {
            displayMonthTime();
        }
    }

    /**
     * Display the monthly overview of time worked by an employee
     */
    private void displayMonthTime() {
        Month month = LocalDate.now().getMonth();
        int days = LocalDate.now().lengthOfMonth();
        int today = LocalDate.now().getDayOfMonth();
        int weeks = LocalDate.now().lengthOfMonth() / 7 + ((days % 7 != 0) ? 1 : 0);

        TableDisplay table = TableDisplay.builder(weeks, 7)
                .enableHeader()
                .enableColumnSeparators()
                .build();

        table.setHeader(List.of("Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"));

        System.out.println(days);

        // This is midnight, start of day
        long date = TimeTable.dateFromTimestamp(Instant.now().getEpochSecond());

        // First of the month, as a timestamp
        long startDate = date - (today - 2) * 86400;

        String[] results = new String[days];
        for (int i = 0; i < days; i++) {
            // Record time worked from midnight to 11:59:59
            Instant instant = Instant.ofEpochSecond(startDate + 86400*i);
            String todaysDate = LocalDate.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MM/dd"));
            results[i] = String.format("%s%s %05.02f",
                    (instant.getEpochSecond() == date ? "*" : " "),
                    todaysDate,
                    TimeTable.timeWorked(employeeID, startDate + 86400*i, startDate + 86400*i + 86399) / 60f / 60f);
        }

        // Finally, set rows
        for (int i = 0; i < weeks; i++) {
            int start = i*7;
            int end = Math.min((i+1)*7, results.length);
            //int end = (i+1)*7;
            LinkedList<String> row = new LinkedList<>(Arrays.asList(Arrays.copyOfRange(results, start, end)));
            // Pad extra row
            while (row.size() < 7) row.add(" -");
            System.out.println(row);

            table.setRow(i, row);
        }

        System.out.println(month.name());
        table.display();
    }

    /**
     * Display the total time today an employee has worked
     */
    private void displayTodayTime() {
        long time = Instant.now().getEpochSecond();
        long total = TimeTable.timeWorked(employeeID, TimeTable.dateFromTimestamp(time), time);

        TableDisplay table = TableDisplay.builder(1, 2)
                .enableHeader()
                .enableColumnSeparators()
                .build();

        table.setHeader(List.of("Date", "Hours"));
        table.setRow(0, List.of(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd E")), String.format("%.2f", total / 60f / 60f)));
        table.display();
    }
}
