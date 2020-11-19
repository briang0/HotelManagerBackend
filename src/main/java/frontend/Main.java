package frontend;

import domain.Security;
import service.ConciergeEntryController;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Entry point into the hotel management system
 * @author Collin
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hotel Chain Manager");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Select a subsystem to access:");
                System.out.println("0: Quit");
                System.out.println("1: Employee Management Console");
                System.out.println("2: Concierge Management Console");
                System.out.println("3: Hotel Manager Console");
                System.out.println("4: Reservations Console");
                System.out.println("5: Inventory Management Console");
                System.out.println("6: Marketing Management Console");
                System.out.println("7: Security Console");
                System.out.print("> ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "0":
                        return;
                    case "1":
                        new EmployeeManagementConsole(scanner).run();
                        break;
                    case "2":
                        new ConciergeManagementConsole(scanner).run();
                        break;
                    case "3":
                        HotelManagementConsole.mainMenu(scanner);
                        break;
                    case "4":
                        RoomManagementConsole.mainMenu(scanner);
                        break;
                    case "5":
                        new InventoryManagementConsole(scanner).run();
                        break;
                    case "6":
                        MarketingConsole.mainMenu(scanner);
                    case "7":
                        SecurityConsole.mainMenu(scanner);
                    default:
                        System.out.println("Unrecognized choice");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}