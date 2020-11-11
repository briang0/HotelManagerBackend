package frontend;

import domain.TimeTable;

import java.util.Scanner;

public class TimeTrackerConsole extends SystemConsole {
    private long employeeID;

    private static final ConsoleSelection clockSelection = ConsoleSelection.builder()
            .add("Check In", "clock_in")
            .add("Check Out", "clock_out")
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
        long total = TimeTable.timeWorked(0, 1604966400);
        System.out.printf("Total worked: %.2f (hrs)\n", total / 60f / 60f);
    }
}
