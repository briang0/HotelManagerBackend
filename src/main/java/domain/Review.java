package domain;

import domain.people.customer.Customer;

import java.util.Date;

/**
 * A class that holds information about a customer review.
 * @author Jack Piscitello
 */

public class Review {
    private Customer customer;
    private Date time;
    private int rating;
    private String review;
    private boolean anonymous;


    /**
     *
     * @param customer The customer associated with the review
     * @param rating The rating - out of 5 stars - that the customer is giving for this review
     * @param review The body of text for the review
     * @param anonymous This signifies whether or not the review is anonymous
     */
    public Review(Customer customer, int rating, String review, boolean anonymous){
        this.customer = customer;
        this.time = new Date();
        this.rating = rating;
        this.review = review;
        this.anonymous = anonymous;
    }

    public Date getTime() {return time;}
    public int getRating() {return rating;}
    public String getReview() {return review;}
    public Customer getCustomer() {return customer;}
    public boolean isAnonymous() {return anonymous;}
}
