package domain.room;

import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Scanner;

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

    public static void registerRate(Scanner scan, RestTemplate restTemplate) {
        System.out.println("=== New Rate ===");
        System.out.println("Cost:");
        float cost = (float) scan.nextDouble();
        System.out.println("Pay Period:");
        int payPeriod = scan.nextInt();
        System.out.println("Currency:");
        int currency = scan.nextInt();
        long rateID = new Random().nextLong();
        String rateuri = "rateId=" + rateID + "&cost=" + cost + "&payPeriod=" + payPeriod + "&currency=" + currency;
        restTemplate.put("http://localhost:8080/rate/create?" + rateuri,String.class);
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
