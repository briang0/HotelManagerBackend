package domain.room;

import java.util.Date;
import java.util.LinkedList;

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
