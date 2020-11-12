package domain;

import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Listing {

    public static void createListing(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter hotelId: ");
        long hotelId = scan.nextLong();
        System.out.println("Enter roomId as a reference for the type of room: ");
        long roomId = scan.nextLong();
        System.out.println("Enter ");
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

    public static void viewListings(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter location keyword. (ex. Iowa, Ames, 50010");
        String key = scan.nextLine();

    }
}
