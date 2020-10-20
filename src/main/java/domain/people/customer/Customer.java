package domain.people.customer;

import domain.room.Reservation;
import domain.room.Room;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represents a hotel customer
 *
 * @Author: Brian Guidarini
 */
public class Customer {

    private LinkedList<Reservation> reservations;
    private Room room;
    private Date dob;
    private String firstName;
    private String lastName;
    private long id;

    /**
     *
     * @param reservations - a list containing the reservation history of the customer
     * @param room - The current room of the customer
     * @param dob - The date of birth for the customer
     * @param firstName - The customer's first name
     * @param lastName - The customer's last name
     * @param id - The customer's unique id
     */
    public Customer(LinkedList<Reservation> reservations, Room room, Date dob, String firstName, String lastName, long id) {
        this.reservations = reservations;
        this.room = room;
        this.dob = dob;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    /**
     * Registers a customer in the database
     * @param scan - A scanner to take in user input
     * @param restTemplate - The rest template to hit the endpoint
     */
    public static void registerCustomer(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== New Customer ===");
        System.out.println("DOB (YYYY-MM-dd):");
        String dob = scan.next();
        System.out.println("First name:");
        String firstName = scan.next();
        System.out.println("Last name:");
        String lastName = scan.next();
        long customerId = new Random().nextLong();
        System.out.println("CustomerId: " + customerId);
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
