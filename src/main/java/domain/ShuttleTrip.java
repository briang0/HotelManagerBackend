package domain;


import java.sql.Date;
import java.sql.Time;

/**
 * A class containing details about a shuttle trip
 * @author Jack Piscitello
 */
public class ShuttleTrip {
    int shuttleId;
    Date date;
    Time leaveTime;
    Time returnTime;
    int locationId;
    float distance;
    int tripId;

}
