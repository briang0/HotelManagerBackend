package service;


import db.Connector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * Controller to handle requests related to activity events
 * @author Jack Piscitello
 */

@RestController
public class ActivityEventController {
    Connection jdbc;

    /**
     * Creates a new event hosted by a hotel
     * @param hotelId ID of the associated hotel
     * @param locationId ID of the location in the system
     * @param date Date of the event
     * @param time1 Start time of the event
     * @param time2 End time of the event
     * @param capacity Max number of participants for the event
     * @param cost Cost per person to participate
     * @param shuttleId ID of the shuttle for the event
     * @return HTTP Status code
     */
    @RequestMapping("/activityEvent/createEvent")
    public String createEvent(@RequestParam(value = "hotelId") long hotelId,
                              @RequestParam(value = "locationId") int locationId,
                              @RequestParam(value = "eventDate") String date,
                              @RequestParam(value = "startTime") String time1,
                              @RequestParam(value = "endTime") String time2,
                              @RequestParam(value = "capacity") int capacity,
                              @RequestParam(value = "cost") float cost,
                              @RequestParam(value = "shuttleId") int shuttleId){
        String query = "INSERT INTO activityEvent VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Date eventDate = Date.valueOf(date);
        Time startTime = Time.valueOf(time1);
        Time endTime = Time.valueOf(time2);
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setInt(1, 0);
            p.setLong(2, hotelId);
            p.setInt(9, shuttleId);
            p.setDate(4, eventDate);
            p.setTime(5, startTime);
            p.setTime(6, endTime);
            p.setInt(3, locationId);
            p.setFloat(8, cost);
            p.setInt(7, capacity);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    @RequestMapping("/activityEvent/listEvents")
    public String listEvents(@RequestParam(value = "hotelId")long hotelId) throws SQLException{
        String query = "SELECT * FROM activityEvent WHERE hotelId=" + hotelId + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        while(rs.next()) {
            int locationId = rs.getInt(3);
            int eventId = rs.getInt(1);
            String date = rs.getDate(4).toString();
            float cost = rs.getFloat(8);
            int capacity = rs.getInt(7);
            int shuttleId = rs.getInt(9);

            sb.append(eventId).append(") Location ID: ").append(locationId).append("\t\tEvent Date: ").append(date).append("\t\t Participation Cost: ");
            sb.append(cost).append("\t\tCapacity: ").append(capacity).append("\t\tShuttle ID: ").append(shuttleId).append("\n\n");
        }
        result = sb.toString();
        return result;
    }

    @RequestMapping("/activityEvent/getEventCost")
    public String getEventCost(@RequestParam(value = "eventId") int eventId) throws SQLException{
        String query = "SELECT * FROM activityEvent WHERE eventId=" + eventId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";

        if(rs.next()){
            result = Float.toString(rs.getFloat(8));
        }
        return result;
    }

    @RequestMapping("/activityEvent/registerForEvent")
    public String registerForEvent(@RequestParam(value = "customerId") long customerId,
                                   @RequestParam(value = "eventId") int eventId,
                                   @RequestParam(value = "participants") int participants,
                                   @RequestParam(value = "cost") float cost){
        String query = "INSERT INTO eventRegistration VALUES(?, ?, ?, ?, ?, ?);";
        float totalCost = cost * participants;
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setInt(1, 0);
            p.setLong(2, customerId);
            p.setInt(3, eventId);
            p.setInt(4, participants);
            p.setFloat(5, totalCost);
            p.setBoolean(6, false);

            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    @RequestMapping("/activityEvent/cancelEvent")
    public String cancelEvent(@RequestParam(value = "eventId") int eventId){
        String query1 = "DELETE FROM activityEvent WHERE eventId=" + eventId + ";";
        String query2 = "DELETE FROM eventRegistration WHERE eventId=" + eventId + ";";
        try{
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query1);
            p.execute();
            p = jdbc.prepareStatement(query2);
            p.execute();
        }

        catch(SQLException e){
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }


}
