package domain.room;

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
