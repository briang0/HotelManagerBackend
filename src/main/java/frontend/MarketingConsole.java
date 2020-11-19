package frontend;

import domain.Listing;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

/**
 * This class provides interaction to market listings
 * @author Brian Guidarini
 */
public class MarketingConsole {

    /**
     * The main menu to give the user choices as to what they want to to with market listings
     * @param scan
     * The scanner to take in user input
     */
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
