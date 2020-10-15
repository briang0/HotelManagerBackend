package frontend;

import service.ConciergeEntryController;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hotel Chain Manager");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Select a subsystem to access:");
                System.out.println("1: Employee Management Console");
                System.out.println("2: Concierge Management Console");
                System.out.print("> ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> new EmployeeManagementConsole(scanner).run();
                    case "2" -> new ConciergeManagementConsole(scanner).run();
                    default -> System.out.println("Unrecognized choice.");
                }
            }
        }
    }
}