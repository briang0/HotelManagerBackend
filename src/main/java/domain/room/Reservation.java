package domain.room;

import domain.people.customer.Customer;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Reservation {

    private Date startDate;
    private Date endDate;
    private Rate rate;
    private Customer customer;
    private Bill bill;

    public Reservation(Date startDate, Date endDate, Rate rate, Customer customer, Bill bill) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.rate = rate;
        this.customer = customer;
        this.bill = bill;
    }

    public static void viewReservations(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== Customer Reservations ===");
        System.out.println("Customer id: ");
        long custId = scan.nextLong();
        String vcuri = "customerId=" + custId;
        String response = restTemplate.getForObject("http://localhost:8080/customer/getReservations?" + vcuri,String.class);
        System.out.println(response);
    }

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
        long billId = 0;
        long registrationId = new Random().nextLong();
        String reguri = "checkInDate=" + checkIn + "&checkOutDate=" + checkout + "&reservationId=" + registrationId + "&customerId=" + customerIdReg + "&rateId=" + rateIdReg + "&billId=" + billId;
        restTemplate.put("http://localhost:8080/reservation/create?" + reguri,String.class);
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

    public Bill getBill() {
        return bill;
    }

}
