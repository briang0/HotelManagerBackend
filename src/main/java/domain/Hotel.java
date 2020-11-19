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
        scan.nextLine();
        System.out.println("Hotel Name:");
        String name = scan.nextLine();

        System.out.println("Address: ");
        String address = scan.nextLine();
        System.out.println("Phone number: ");
        String phone = scan.nextLine();
        long hotelId = Math.abs(new Random().nextLong());
        System.out.println("HotelId: " + hotelId);
        String uri = "address=" + address + "&hotelId=" + hotelId + "&phone=" + phone + "&name=" + name;
        restTemplate.put("http://localhost:8080/hotel/create?" + uri,String.class);
        Inventory.create(hotelId);
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

    /**
     * Displays the response for the getAllHotels endpoint
     * @param scan
     * The scanner to take in input
     * @param restTemplate
     * The restTemplate to interact with the endpoint
     */
    public static void getAllHotels(Scanner scan, RestTemplate restTemplate) {
        String response = restTemplate.getForObject("http://localhost:8080/hotel/getAllHotels",String.class);
        System.out.println(response);
    }

    /**
     * Select a hotel from a list
     * @param scan Scanner to take an input
     * @param restTemplate RestTemplate to interact with the endpoint
     * @return The ID of the hotel selected
     */
    public static Long selectHotel(Scanner scan, RestTemplate restTemplate){
        System.out.println("Select a hotel from the following list:");
        getAllHotels(scan, restTemplate);
        System.out.println("Select a number representing a hotel:");
        int index = scan.nextInt();
        scan.nextLine();
        String hotelId = restTemplate.getForObject("http://localhost:8080/hotel/getId?hotelIndex="+index,String.class);
        return Long.parseLong(hotelId);
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
