package domain;

import frontend.MarketingListingConsole;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Brian Guidarini
 *
 * This class ties the frontend and the backend together for the listing functionality
 *
 */
public class Listing {

    /**
     * Allows the user to create a market listing
     * @param scan
     *  The scanner used to take in input
     * @param restTemplate
     *  The restTemplate for the http request
     */
    public static void createListing(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter hotelId: ");
        long hotelId = scan.nextLong();
        System.out.println("Enter roomId as a reference for the type of room: ");
        long roomId = scan.nextLong();
        System.out.println("Select a picture to upload ");
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String selection = dialog.getDirectory() + dialog.getFile();
        scan.nextLine();
        System.out.println("Enter a brief description of the rooms. 1000 characters max. ");
        String description = scan.nextLine();
        String uri = "http://localhost:8080/listing/create?hotelId=" + hotelId + "&roomId=" + roomId + "&imageDir=" + selection + "&description=" + description;
        restTemplate.put(uri, String.class);
    }

    /**
     * The function that allows listings to be viewed by a potential customer
     * @param scan
     * The scanner to take in user input
     * @param restTemplate
     * The Rest Template to interact with the web api
     */
    public static void viewListings(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter location keyword. (ex. Iowa, Ames, 50010)");
        scan.nextLine();
        String key = scan.nextLine();
        String uri = "http://localhost:8080/listing/getAll?key=" + key;
        String data = restTemplate.getForObject(uri, String.class);
        LinkedList<ListingContainer> items = new LinkedList<>();
        if (data != null) {
            String[] dataArr = data.split("\n");
            int factor = 9;
            int numListings = dataArr.length / factor;
            for (int i = 0; i < numListings; i++) {
                String description = dataArr[i * factor];
                float cost = Float.parseFloat(dataArr[i * factor + 1]);
                int payPeriod = Integer.parseInt(dataArr[i * factor + 2]);
                String roomDescription = dataArr[i *factor + 3];
                String address = dataArr[i * factor + 4];
                String phone = dataArr[i * factor + 5];
                long hotelId = Long.parseLong(dataArr[i * factor + 6]);
                long roomId = Long.parseLong(dataArr[i * factor + 7]);
                long rateId = Long.parseLong(dataArr[i * factor + 8]);
                ListingContainer listing = new ListingContainer(description, cost, payPeriod, roomDescription, address, phone, hotelId, roomId, rateId);
                try {
                    listing.loadBufferedImageFromDb();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                items.add(listing);
                MarketingListingConsole.mainMenu(scan, items);
            }
        } else{
            System.out.println("No matches");
        }

    }
}
