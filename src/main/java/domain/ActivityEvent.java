package domain;

import java.sql.Date;
import java.sql.Time;

/**
 * A class containing information about an activity event
 * @author Jack Piscitello
 */
public class ActivityEvent {
    private long hotelId;
    private int locationId;
    private int eventId;
    private Date eventDate;
    private Time startTime;
    private Time endTime;
    private int capacity;
    private float costPerPerson;
    private int shuttleId;
}
