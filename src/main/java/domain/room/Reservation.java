package domain.room;

import domain.people.customer.Customer;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represents a customer reservtion for a particular room.
 * @Author: Brian Guidarini
 */
public class Reservation {

    private Date startDate;
    private Date endDate;
    private Rate rate;
    private Customer customer;

    /**
     *
     * @param startDate The start date/time of the check in
     * @param endDate The end date/time for check out
     * @param rate The monetary  rate of the room
     * @param customer The customer involved in the reservation
     */
    public Reservation(Date startDate, Date endDate, Rate rate, Customer customer) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rate = rate;
        this.customer = customer;
    }

    /**
     * Get the reservations for a given customer
     * @param scan - Scanner to take in user input
     * @param restTemplate - RestTemplate to interact with the web api
     */
    public static void viewReservations(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== Customer Reservations ===");
        System.out.println("Customer id: ");
        long custId = scan.nextLong();
        String vcuri = "customerId=" + custId;
        String response = restTemplate.getForObject("http://localhost:8080/customer/getReservations?" + vcuri,String.class);
        System.out.println(response);
    }

    /**
     * Creates a reservation for a customer
     * @param scan A scanner for the user input
     * @param restTemplate RestTemplate to interact with the web api
     */
    public static void createReservation(Scanner scan, RestTemplate restTemplate) {
        System.out.println("== Create reservation ==");
        System.out.println("Check in time: yyyy-MM-ddxHH:mm:ss");
        String checkIn = scan.next();
        checkIn = checkIn.replaceAll("x", " ");
        System.out.println("Checkout time:");
        String checkout = scan.next();
        checkout = checkout.replaceAll("x", " ");
        System.out.println(checkIn + " " + checkout);
        System.out.println("Customer id:");
        long customerIdReg = scan.nextLong();
        System.out.println("Rate ID");
        long rateIdReg = scan.nextLong();
        System.out.println("Room ID");
        long roomId = scan.nextLong();
        long billId = 0;
        long registrationId = new Random().nextLong();
        System.out.println("RegistrationId: " + registrationId);
        String reguri = "checkInDate=" + checkIn + "&checkOutDate=" + checkout + "&reservationId=" + registrationId + "&customerId=" + customerIdReg + "&rateId=" + rateIdReg + "&billId=" + billId + "&roomId=" + roomId;
        restTemplate.put("http://localhost:8080/reservation/create?" + reguri,String.class);
    }

    public static void viewAllCurrentReservationsForHotel(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter hotel ID");
        long hotelId = scan.nextLong();
        String response = restTemplate.getForObject("http://localhost:8080/reservation/getAllActiveReservationsAssociatedToHotel?hotelId=" + hotelId,String.class);
        System.out.println(response);
    }

    public static void viewAllReservationsForHotel(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter hotel ID");
        long hotelId = scan.nextLong();
        String response = restTemplate.getForObject("http://localhost:8080/reservation/getAllReservationsAssociatedToHotel?hotelId=" + hotelId,String.class);
        System.out.println(response);
    }

    public static void updateWakeupTime(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Wakeup time: yyyy-MM-ddxHH:mm:ss");
        String wakeup = scan.next();
        wakeup = wakeup.replaceAll("x", " ");
        System.out.println("Enter roomId");
        long reservationId = scan.nextLong();
        String reguri = "wakeUpTime=" + wakeup + "&roomId=" + reservationId;
        restTemplate.put("http://localhost:8080/reservation/setWakeupTime?" + reguri,String.class);
    }

    public static void markReservationAsPaid(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Reservation ID: ");
        long reservationId = scan.nextLong();
        restTemplate.put("http://localhost:8080/reservation/markAsPaid?reservationId=" + reservationId,String.class);
    }

    public static void markAllReservationAsPaid(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Customer ID: ");
        long customerId = scan.nextLong();
        restTemplate.put("http://localhost:8080/reservation/markAllAsPaid?customerId=" + customerId,String.class);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Rate getRate() {
        return rate;
    }

    public Customer getCustomer() {
        return customer;
    }

}
