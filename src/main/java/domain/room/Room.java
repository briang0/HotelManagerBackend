package domain.room;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

public abstract class Room {

    private Reservation reservation;
    private LinkedList<Reservation> reservationHistory;
    private Date wakeUpTime;
    private int roomNumber;
    private UUID id;

    public Room(Reservation reservation, LinkedList<Reservation> reservationHistory, int roomNumber, UUID id) {
        this.reservation = reservation;
        this.reservationHistory = reservationHistory;
        wakeUpTime = null;
    }

    public Reservation getRate() {
        return reservation;
    }

}
