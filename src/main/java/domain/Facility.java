package domain;

import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.LinkedList;
import java. util.Scanner;

/**
 * An object representing a hotel facility
 * @author Jack Piscitello
 */

public class Facility {
    private String facilityName;
    private String facilityDescription;
    private long facilityId;
    private float rentRate;
    private LinkedList<FacilityReservation> reservationHistory;
    private long hotelId;


    /**
     * @param facilityName Name of the facility, general descriptor
     * @param desc Description of the facility, can include more specific details
     * @param facilityId Unique ID for the facility
     * @param rate Cost of renting this particular facility for one hour
     * @param hotelId ID of the hotel the facility is located at
     */

    public Facility(String facilityName, String desc, long facilityId, float rate, long hotelId){
        this.facilityName = facilityName;
        this.facilityDescription = desc;
        this.facilityId = facilityId;
        this.rentRate = rate;
        this.reservationHistory = null;
        this.hotelId = hotelId;

    }


    public long getFacilityId() {return facilityId;}
    public float getRentRate() {return rentRate;}
    public String getFacilityName() {return facilityName; }
    public LinkedList<FacilityReservation> getReservationHistory() {return reservationHistory;}
    public String getFacilityDescription() {return facilityDescription;}
    public long getHotelId() {return hotelId;}
}


