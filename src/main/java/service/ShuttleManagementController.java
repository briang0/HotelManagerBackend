package service;


import db.Connector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * Controller for handling requests related to shuttle management
 * @author Jack Piscitello
 */
@RestController
public class ShuttleManagementController {
    Connection jdbc;

    /**
     * Creates a new shuttle in the database
     * @param hotelId
     * @param capacity
     * @return
     */
    @RequestMapping("/shuttle/createShuttle")
    public String createShuttle(@RequestParam(value = "hotelId") long hotelId,
                                @RequestParam(value = "capacity") int capacity){
        String query = "INSERT INTO shuttle VALUES(?, ?, ?);";

        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setInt(2, capacity);
            p.setLong(3, hotelId);
            p.setInt(1, 0);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Views all shuttles for a hotel
     * @param hotelId
     * @return
     * @throws SQLException
     */
    @RequestMapping("shuttle/viewShuttles")
    public String viewShuttles(@RequestParam(value = "hotelId") long hotelId) throws SQLException{
        String query = "SELECT * FROM shuttle WHERE hotelId=" + hotelId;

        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        while(rs.next()) {
            int shuttleId = rs.getInt(1);
            int capacity = rs.getInt(2);

            sb.append("Shuttle ID: ").append(shuttleId).append("\t\tPassenger capacity:").append(capacity).append("\n");
        }
        result = sb.toString();
        return result;
    }

    /**
     * Deletes a shuttle from the DB
     * @param shuttleId
     * @return
     */
    @RequestMapping("/shuttle/deleteShuttle")
    public String deleteShuttle(@RequestParam(value = "shuttleId") int shuttleId){
        String query = "DELETE FROM shuttle WHERE shuttleId=" + shuttleId;
        try{
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Creates a new shuttle trip in the DB
     * @param shuttleId
     * @param date
     * @param time1
     * @param time2
     * @param locationId
     * @param distance
     * @return
     */
    @RequestMapping("/shuttleTrip/createTrip")
    public String createTrip(@RequestParam(value = "shuttleId") int shuttleId,
                             @RequestParam(value = "tripDate") String date,
                             @RequestParam(value = "leaveTime") String time1,
                             @RequestParam(value = "returnTime") String time2,
                             @RequestParam(value = "locationId") int locationId,
                             @RequestParam(value = "distance") float distance) {
        String query = "INSERT INTO shuttleTrip VALUES(?, ?, ?, ?, ?, ?, ?);";
        Date tripDate = Date.valueOf(date);
        Time leaveTime = Time.valueOf(time1);
        Time returnTime = Time.valueOf(time2);
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setInt(1, 0);
            p.setInt(2, shuttleId);
            p.setDate(3, tripDate);
            p.setTime(4, leaveTime);
            p.setTime(5, returnTime);
            p.setInt(6, locationId);
            p.setFloat(7, distance);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Views a schedule for a shuttle on a given date
     * @param shuttleId
     * @param date
     * @return
     * @throws SQLException
     */
    @RequestMapping("/shuttleTrip/viewSchedule")
    public String viewSchedule(@RequestParam(value = "shuttleId") int shuttleId,
                               @RequestParam(value = "date") String date) throws SQLException{
        String query = "SELECT * FROM shuttleTrip WHERE shuttleId=" + shuttleId + " AND tripDate='" + date + "' ORDER BY leaveTime ASC;";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "Schedule for Shuttle " + shuttleId + " on " + date + ":\n";
        StringBuilder sb = new StringBuilder(result);

        while(rs.next()) {
            int tripId = rs.getInt(1);
            Time leaveTime = rs.getTime(4);
            Time returnTime = rs.getTime(5);
            int locationId = rs.getInt(6);
            float distance = rs.getFloat(7);

            sb.append("Trip ID: ").append(tripId).append("\t\tDeparture time: ").append(leaveTime).append("\t\tReturn time: ");
            sb.append(returnTime).append("\t\tLocation ID: ").append(locationId).append("\t\tDistance from hotel: ");
            sb.append(distance).append(" mi").append("\n");
        }
        result = sb.toString();
        return result;
    }

    /**
     * Deletes a shuttle trip from the DB
     * @param tripId
     * @return
     */
    @RequestMapping("/shuttleTrip/deleteTrip")
    public String deleteTrip(@RequestParam(value = "tripId") int tripId){
        String query = "DELETE FROM shuttleTrip WHERE tripId=" + tripId;
        try{
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.execute();
        }
        catch(SQLException e){
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Gets the shuttle's capacity
     * @param shuttleId
     * @return
     * @throws SQLException
     */
    @RequestMapping("/shuttle/getShuttleCapacity")
    public String getShuttleCapacity(@RequestParam(value = "shuttleId") int shuttleId) throws SQLException{
        String query = "SELECT * FROM shuttle WHERE shuttleId=" + shuttleId + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";

        if(rs.next()) {
            result = Integer.toString(rs.getInt(2));
        }
        return result;
    }

}
