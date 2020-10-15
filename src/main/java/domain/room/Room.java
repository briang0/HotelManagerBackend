package domain.room;

import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Room {

    private LinkedList<Reservation> reservationHistory;
    private Date wakeUpTime;
    private int roomNumber;
    private long id;
    private boolean housekeepingStatus;
    private String roomDescription;
    private long rateId;

    public Room(int roomNumber, long id, String roomDescription, long rateId) {
        this.reservationHistory = null;
        this.id = id;
        this.roomNumber = roomNumber;
        wakeUpTime = null;
        housekeepingStatus = false;
        this.roomDescription = roomDescription;
    }

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
        long id = new Random().nextLong();
        String ruri = "roomNumber=" + roomNumber + "&roomId=" + id + "&houseKeepingStatus=" + false + "&roomDescription=" + roomDescription + "&rateId=" + rateId + "&hotelId=" + rhotelId;
        restTemplate.put("http://localhost:8080/room/create?" + ruri,String.class);
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
