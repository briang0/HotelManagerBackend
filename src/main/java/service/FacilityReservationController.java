package service;

import db.Connector;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

/**
 * A controller class to connect the frontend to the database
 * @author Jack Piscitello
 */
@RestController
public class FacilityReservationController {
    Connection jdbc;

    /**
     * Creates a new facility reservation
     * @param facilityId ID of the facility being reserved
     * @param startTime Start time of the reservation
     * @param endTime End time of the reservation
     * @param customerId ID of the customer reserving the facility
     * @param reservationId Unique ID for the reservation
     * @return HTTP status code
     * @throws ParseException
     */
    @RequestMapping("/facilityReservation/create")
    public String createFacilityReservation(@RequestParam(value = "facilityId") long facilityId,
                                            @RequestParam(value = "startTime") String startTime,
                                            @RequestParam(value = "endTime") String endTime,
                                            @RequestParam(value = "customerId") long customerId,
                                            @RequestParam(value = "reservationId") long reservationId) throws ParseException {

        String query = "INSERT INTO facilityReservation VALUES(?, ?, ?, ?, ?);";

        java.util.Date start = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
        Date end = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss");

        java.sql.Timestamp startStamp = new java.sql.Timestamp(start.getTime());
        java.sql.Timestamp endStamp = new java.sql.Timestamp(end.getTime());


        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, facilityId);
            p.setTimestamp(2, startStamp);
            p.setTimestamp(3, endStamp);
            p.setLong(4, customerId);
            p.setLong(5, reservationId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";

    }

    /**
     * Deletes an existing facility reservation
     * @param facilityReservationId ID of the reservation to be deleted
     * @return HTTP Status Code
     */
    @RequestMapping("/facilityReservation/delete")
    public String deleteFacilityReservation(@RequestParam(value = "facilityReservationId") long facilityReservationId){
        String query = "DELETE FROM facilityReservation WHERE facilityReservationId = " + facilityReservationId;
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
     * Show details of a specific facility reservation
     * @param facilityReservationId ID of the reservation being shown
     * @return String with details of the reservation
     * @throws SQLException
     */
    @RequestMapping("/facilityReservation/get")
    public String showFacilityReservation(@RequestParam(value = "facilityReservationId") long facilityReservationId) throws SQLException {

        String query = "SELECT * FROM facilityReservation where facilityReservationId =" + facilityReservationId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        if(rs.next()) {
            long facilityId = rs.getLong(1);
            String startTime = rs.getTimestamp(2).toString();
            String endTime = rs.getTimestamp(3).toString();
            long customerId = rs.getLong(4);

            sb.append("Reservation ID: ").append(facilityReservationId).append("\nStart Time: ").append(startTime).append("\nEnd Time: ");
            sb.append(endTime).append("\nFacility ID: ").append(facilityId).append("\nCustomer ID: ").append(customerId).append("\n");
            result = sb.toString();
        }
        return result;
    }

    /**
     * List all reservations associated with a given facility
     * @param facilityId ID of the facility for which the reservations are being listed
     * @return String listing reservations
     * @throws SQLException
     */
    @RequestMapping("/facilityReservation/list")
    public String listFacilityReservations(@RequestParam(value = "facilityId") long facilityId) throws SQLException{
        String query = "SELECT * FROM facilityReservation where facilityId =" + facilityId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        while(rs.next()) {
            long reservationId = rs.getLong(5);
            String startTime = rs.getTimestamp(2).toString();

            sb.append("Start Time: ").append(startTime).append("\t\tReservation ID: ").append(reservationId).append("\n");
        }
        result = sb.toString();
        return result;
    }
}
