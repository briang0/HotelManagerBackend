package domain.people.customer;

import domain.room.Reservation;
import domain.room.Room;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

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

    public static void registerCustomer(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== New Customer ===");
        System.out.println("DOB (YYYY-MM-dd):");
        String dob = scan.next();
        System.out.println("First name:");
        String firstName = scan.next();
        System.out.println("Last name:");
        String lastName = scan.next();
        long customerId = new Random().nextLong();
        String curi = "dob=" + dob + "&firstName=" + firstName + "&lastName=" + lastName + "&customerId=" + customerId;
        restTemplate.put("http://localhost:8080/customer/create?" + curi,String.class);
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
