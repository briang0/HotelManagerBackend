package domain;

import domain.people.customer.Customer;

import java.util.Date;

/**
 * Class that holds information about a particular facility reservation
 *
 * @author Jack Piscitello
 */


public class FacilityReservation {
    private Date startTime;
    private Date endTime;
    private Customer customer;
    private Facility facility;
    private long reservationId;

    /**
     * Constructs a FacilityReservation object
     * @param startTime Start time of the reservation
     * @param endTime Time the reservation ends
     * @param customer Customer being billed for the
     * @param facility
     * @param reservationId
     */
    public FacilityReservation(Date startTime, Date endTime, Customer customer, Facility facility, long reservationId){
        this.startTime = startTime;
        this.endTime = endTime;
        this.customer = customer;
        this.facility = facility;
        this.reservationId = reservationId;
    }

    public Customer getCustomer() {return customer;}
    public Date getEndTime() {return endTime;}
    public Date getStartTime() {return startTime;}
    public Facility getFacility() {return facility;}
    public long getReservationId() {return reservationId;}
}
