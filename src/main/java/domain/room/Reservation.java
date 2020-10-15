package domain.room;

import domain.people.customer.Customer;

import java.util.Date;

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
