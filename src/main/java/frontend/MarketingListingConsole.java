package frontend;

import domain.ListingContainer;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * This class provides the console for how a user wants interact with a SPECIFIC listing
 * @author Brian Guidarini
 */
public class MarketingListingConsole {

    /**
     * The main screen view
     * @param scan
     * The scanner to take in user input
     * @param listings
     * The listings that matched the user's search in the MarketingConsole
     */
    public static void mainMenu(Scanner scan, LinkedList<ListingContainer> listings) {
        System.out.println("Select a listing: ");
        int choice = -1;
        while (choice != 0) {
            System.out.println("0) quit");
            for (int i = 0; i < listings.size(); i++) {
                System.out.println((i+1) + ")");
                System.out.println(listings.get(i));
            }
            choice = scan.nextInt();
            if ((choice - 1) >= listings.size() || choice < 0) {
                System.out.println("Invalid choice");
            } else {
                secondaryMenu(scan, listings.get(choice - 1));
            }
        }
    }

    /**
     * This menu is to either show an image of a listing or book it
     * @param scan
     * The scanner to take in input
     * @param listing
     * A single listing that the user selected in the main menu
     */
    public static void secondaryMenu(Scanner scan, ListingContainer listing) {
        System.out.println("You selected: ");
        System.out.println(listing);
        int choice = -1;
        while (true) {
            System.out.println("0) go back");
            System.out.println("1) View an image of this room");
            System.out.println("2) Book this room");
            choice = scan.nextInt();
            switch (choice) {
                case 0:
                    return;
                case 1:
                    listing.viewImage();
                    break;
                case 2:
                    listing.book(scan);
                    break;
                default:
                    System.out.println("Invalid selection");
            }
        }
    }
}
