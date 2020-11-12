package frontend;

import domain.Listing;
import domain.people.customer.Customer;
import domain.room.Reservation;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

import static domain.room.Reservation.viewAllCurrentReservationsForHotel;
import static domain.room.Reservation.viewAllReservationsForHotel;

public class MarketingConsole {
    public static void mainMenu(Scanner scan) {
        int choice = -1;
        //Scanner scan = new Scanner(System.in);
        RestTemplate restTemplate = new RestTemplate();
        while (choice != 0) {
            System.out.println("=== Room Management Console");
            System.out.println("0) exit");
            System.out.println("1) Make Listing");
            System.out.println("2) View Listings");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    Listing.createListing(scan, restTemplate);
                    break;
                case 2:
                    Listing.viewListings(scan, restTemplate);
            }
        }
    }
}
