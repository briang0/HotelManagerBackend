package domain;

import domain.people.customer.Customer;

import java.util.Date;

/**
 * A class containting information regarding a customer complaint
 * @author Jack Piscitello
 */

public class CustomerComplaint {
    public enum Status{ACTIVE, NEW_COMPLAINT, CLOSED};

    private Customer customer;
    private Date time;
    private Status status;
    private String complaintDetails;
    private float refund;
    private Employee employee;

    /**
     *
     * @param customer Customer making the current complaint
     * @param complaintDetails The body of text describing the initial complaint
     * @param refund The amount of money being refunded to the customer, if any
     * @param employee The employee assigned to resolve the complaint
     */
    public CustomerComplaint(Customer customer, String complaintDetails, float refund, Employee employee){
        this.customer = customer;
        this.time = new Date();
        this.status = Status.NEW_COMPLAINT;
        this.complaintDetails = complaintDetails;
        this.refund = refund;
        this.employee = employee;
    }

    public Customer getCustomer() {return customer;}
    public Date getTime() {return time;}
    public Employee getEmployee() {return employee;}
    public float getRefund() {return refund;}
    public Status getStatus() {return status;}
    public String getComplaintDetails() {return complaintDetails;}
}
