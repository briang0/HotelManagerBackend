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
                System.out.println("8: Event Management Console");
                System.out.println("9: Customer Management Console");
                System.out.println("10: Shuttle Management Console");
                System.out.println("11: Customer Feedback Console");
                System.out.println("12: Mail Console");
                System.out.println("13: Time Tracker Console");
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
                        System.out.println("=== Reservation Console ===");
                        System.out.println("0: Exit");
                        System.out.println("1: Room Reservation Console");
                        System.out.println("2: Facility Reservation Console");
                        int check = scanner.nextInt();
                        scanner.nextLine();
                        if(check == 1)
                            RoomManagementConsole.mainMenu(scanner);
                        else if(check == 2)
                            FacilityManagementConsole.facilityReservationManagementMenu(scanner);
                        break;
                    case "5":
                        new InventoryManagementConsole(scanner).run();
                        break;
                    case "6":
                        MarketingConsole.mainMenu(scanner);
                        break;
                    case "7":
                        SecurityConsole.mainMenu(scanner);
                        break;
                    case "8":
                        ActivityPlanningConsole.activityPlanningMenu(scanner);
                        break;
                    case "9":
                        RoomManagementConsole.customerMenu(scanner);
                        break;
                    case "10":
                        ShuttleManagementConsole.shuttleManagementMenu(scanner);
                        break;
                    case "11":
                        CustomerFeedbackConsole.customerFeedbackMenu(scanner);
                        break;
                    case "12":
                        new MailManagementConsole(scanner).run();
                        break;
                    case "13":
                        new TimeTrackerConsole(scanner).run();
                        break;
                    default:
                        System.out.println("Unrecognized choice");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}