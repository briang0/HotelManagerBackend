package frontend;

import domain.people.customer.Customer;
import domain.room.Reservation;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

/**
 * A class to allow for interaction with the rooms
 * @Author: Brian Guidarini
 */
public class RoomManagementConsole {

    /**
     * The main menu look for this particular menu
     * @param scan A scanner to take in user input
     */
    public static void mainMenu(Scanner scan) {
        int choice = -1;
        //Scanner scan = new Scanner(System.in);
        RestTemplate restTemplate = new RestTemplate();
        while (choice != 0) {
            System.out.println("=== Room Management Console");
            System.out.println("0) exit");
            System.out.println("1) Register Customer");
            System.out.println("2) Make Reservation");
            System.out.println("3) View Customer Reservations");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    Customer.registerCustomer(scan, restTemplate);
                    break;
                case 2:
                    Reservation.createReservation(scan, restTemplate);
                    break;
                case 3:
                    Reservation.viewReservations(scan, restTemplate);
            }
        }
    }

}
