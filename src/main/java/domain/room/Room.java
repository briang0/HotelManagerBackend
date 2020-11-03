package domain.room;

import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * A class to represent a hotel room
 * @Author: Brian Guidarini
 */
public class Room {

    private LinkedList<Reservation> reservationHistory;
    private Date wakeUpTime;
    private int roomNumber;
    private long id;
    private boolean housekeepingStatus;
    private String roomDescription;
    private long rateId;

    /**
     *
     * @param roomNumber The number of the room in the hotel
     * @param id The unique id for the room
     * @param roomDescription The description of the room, such as suite or room or penthouse
     * @param rateId The id for the rate associated with this room
     */
    public Room(int roomNumber, long id, String roomDescription, long rateId) {
        this.reservationHistory = null;
        this.id = id;
        this.roomNumber = roomNumber;
        wakeUpTime = null;
        housekeepingStatus = false;
        this.roomDescription = roomDescription;
    }

    /**
     * This method registers a room in the database
     * @param scan A scanner object to take in user input
     * @param restTemplate A RestTemplate to interact with the web api
     */
    public static void registerRoom(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== New Room ===");
        System.out.println("Room number: ");
        int roomNumber = scan.nextInt();
        System.out.println("Room description:");
        String roomDescription = scan.next();
        System.out.println("Rate id:");
        long rateId = scan.nextLong();
        System.out.println("Hotel id:");
        long rhotelId = scan.nextLong();
        long id = Math.abs(new Random().nextLong());
        System.out.println("RoomID: " + id);
        String ruri = "roomNumber=" + roomNumber + "&roomId=" + id + "&houseKeepingStatus=" + false + "&roomDescription=" + roomDescription + "&rateId=" + rateId + "&hotelId=" + rhotelId;
        restTemplate.put("http://localhost:8080/room/create?" + ruri,String.class);
    }

    /**
     * Calls the createRoom endpoint multiple times to create the specified number of rooms given the specified constraints
     * @param scan
     * The scanner to take in user input
     * @param restTemplate
     * The restTemplate to interact with the endpoint
     */
    public static void registerMultipleRooms(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Room description for each room");
        String roomClusterDescription = scan.next();
        System.out.println("First room");
        int roomStart = scan.nextInt();
        System.out.println("Ending room number");
        int roomEnd = scan.nextInt();
        System.out.println("Room increment");
        int roomIncrement = scan.nextInt();
        System.out.println("RateID");
        long rateId = scan.nextLong();
        System.out.println("HotelId");
        long hotelId = scan.nextLong();

        for (int i = roomStart; i <= roomEnd; i+=roomIncrement) {
            long id = new Random().nextLong();
            String ruri = "roomNumber=" + i + "&roomId=" + id + "&houseKeepingStatus=" + false + "&roomDescription=" + roomClusterDescription + "&rateId=" + rateId + "&hotelId=" + hotelId;
            restTemplate.put("http://localhost:8080/room/create?" + ruri, String.class);
        }
    }

    public LinkedList<Reservation> getReservationHistory() {
        return reservationHistory;
    }

    public Date getWakeUpTime() {
        return wakeUpTime;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public long getId() {
        return id;
    }

    public boolean isHousekeepingStatus() {
        return housekeepingStatus;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public long getRateId() {
        return rateId;
    }
 }
