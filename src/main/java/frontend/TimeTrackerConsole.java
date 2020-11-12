package frontend;

import domain.TimeTable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TimeTrackerConsole extends SystemConsole {
    private long employeeID;

    private static final ConsoleSelection clockSelection = ConsoleSelection.builder()
            .add("Check In", "clock_in")
            .add("Check Out", "clock_out")
            .build();

    private static final ConsoleSelection timeViewSelection = ConsoleSelection.builder()
            .add("Today", "today")
            .add("Month", "month")
            .add("Year", "year")    // multiple tables
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
        employeeID = Long.parseLong(scanner.nextLine());
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

    }

    public void clock() {
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

    public void time() {
        // how about a today command, that does this stuff.
        String answer = timeViewSelection.select(scanner);

        if (answer.equals("today")) {
            displayTodayTime();
        }

        int weeks = LocalDate.now().lengthOfMonth() / 7;

        TableDisplay table = new TableDisplay(weeks, 7);
        table.setHeader(List.of("Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"));
        // Then, just iterate through timestamps, start of day to end of day query via TimeTable,
        // and display as 'MM/dd {hours:.2f}'
        // Obtain timestamp data via time api
        // try and mark today's date with something (could try colored output in intellij)
        // Use '-' for empty dates
        System.out.println(LocalDate.now().getMonth().name());
        table.display();
    }

    private void displayTodayTime() {
        long time = Instant.now().getEpochSecond();
        long total = TimeTable.timeWorked(TimeTable.dateFromTimestamp(time), time);

        TableDisplay table = new TableDisplay(1, 2);
        table.setHeader(List.of("Date", "Hours"));
        table.setRow(0, List.of(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd E")), String.format("%.2f", total / 60f / 60f)));
        table.display();
    }
}
