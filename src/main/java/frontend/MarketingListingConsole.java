package frontend;

import domain.ListingContainer;

import java.util.LinkedList;
import java.util.Scanner;

public class MarketingListingConsole {

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

    public static void secondaryMenu(Scanner scan, ListingContainer listing) {
        System.out.println("You selected: ");
        System.out.println(listing);
        int choice = -1;
        while (true) {
            System.out.println("0) go back");
            System.out.println("1) View an image of this room");
            System.out.print("2) Book this room");
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
