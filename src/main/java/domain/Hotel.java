package domain;

import domain.room.Room;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * An object representing a hotel
 * @Author: Brian Guidarini
 */
public class Hotel {

    private LinkedList<Room> rooms;
    private long hotelId;
    private String address;

    /**
     *
     * @param hotelId The unique ID of the hotel
     * @param address The address of the hotel
     */
    public Hotel(long hotelId, String address) {
        rooms = new LinkedList<>();
        this.hotelId = hotelId;
        this.address = address;
    }

    /**
     * Registers a hotel in the database
     * @param scan A Scanner that takes in user input
     * @param restTemplate A RestTemplate to interact with the web api
     */
    public static void registerHotel(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== New Hotel ===");
        System.out.println("Address: ");
        scan.nextLine();
        String address = scan.nextLine();
        long hotelId = new Random().nextLong();
        String uri = "address=" + address + "&hotelId=" + hotelId;
        restTemplate.put("http://localhost:8080/hotel/create?" + uri,String.class);
    }

    /**
     * This method displays each room in the hotel
     * @param scan A Scanner to take in user input
     * @param restTemplate A RestTemplate to interact with the web api
     */
    public static void viewRooms(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Hotel ID: ");
        long vhotelId = scan.nextLong();
        String vuri = "hotelId=" + vhotelId;
        String response = restTemplate.getForObject("http://localhost:8080/hotel/get?" + vuri,String.class);
        System.out.println(response);
    }

    public LinkedList<Room> getRooms() {
        return rooms;
    }

    public long getHotelId() {
        return hotelId;
    }

    public String getAddress() {
        return address;
    }
}
