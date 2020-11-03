package domain.room;

import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Scanner;

/**
 * This class represents the rate of a room. In the sense of how often payments need to be made, what currency,
 * and the amount of money
 * @Author: Brian Guidarini
 */
public class Rate {

    public final static int USD = 1;

    public final static int PERIOD_DAILY = 1;
    public final static int PERIOD_WEEKLY = 7;
    public final static int PERIOD_MONTHLY = 30;
    public final static int PERIOD_ANNUAL = 365;

    private float cost;
    private int payPeriod;
    private int currency;

    /**
     *
     * @param cost
     * The monetary value of each pay period
     * @param payPeriod
     * The number of days per period
     * @param currency
     * The currency of the cost
     */
    public Rate(float cost, int payPeriod, int currency) {
        this.cost = cost;
        this.payPeriod = payPeriod;
        this.currency = currency;
    }

    /**
     *
     * @param scan A scanner to take in user input
     * @param restTemplate A rest template to interact with the web api
     */
    public static void registerRate(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== New Rate ===");
        System.out.println("Cost:");
        float cost = (float) scan.nextDouble();
        System.out.println("Pay Period:");
        int payPeriod = scan.nextInt();
        System.out.println("Currency:");
        int currency = scan.nextInt();
        long rateID = new Random().nextLong();
        System.out.println("RateID: " + rateID);
        String rateuri = "rateId=" + rateID + "&cost=" + cost + "&payPeriod=" + payPeriod + "&currency=" + currency;
        restTemplate.put("http://localhost:8080/rate/create?" + rateuri,String.class);
    }

    /**
     * Gets all rates associated to a hotel
     * @param scan
     * A scanner to take in user input
     * @param restTemplate
     * A rest template to interact with the endpoint
     */
    public static void getAllRatesAssociatedToHotel(Scanner scan, RestTemplate restTemplate) {
        System.out.println("HotelID: ");
        long hotelId = scan.nextLong();
        String rateuri = "hotelId=" + hotelId;
        String response = restTemplate.getForObject("http://localhost:8080/rate/getAllRatesAssociatedToHotel?" + rateuri,String.class);
        System.out.println(response);
    }

    public float getCost() {
        return cost;
    }

    public int getPayPeriod() {
        return payPeriod;
    }

    public int getCurrency() {
        return currency;
    }
}
