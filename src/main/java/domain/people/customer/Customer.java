package domain.people.customer;

import domain.room.Reservation;
import domain.room.Room;

import java.sql.Date;
import java.util.LinkedList;

public class Customer {

    private LinkedList<Reservation> reservations;
    private Room room;
    private Date dob;
    private String firstName;
    private String lastName;
    private long id;

    public Customer(LinkedList<Reservation> reservations, Room room, Date dob, String firstName, String lastName, long id) {
        this.reservations = reservations;
        this.room = room;
        this.dob = dob;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public LinkedList<Reservation> getReservations() {
        return reservations;
    }

    public Room getRoom() {
        return room;
    }

    public Date getDob() {
        return dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getId() {
        return id;
    }
}
