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
public class FacilitiesController {
    Connection jdbc;

    /**
     * Creates a new facility in the given hotel
     * @param facilityName Name of the facility
     * @param description Details of the facility
     * @param facilityId Unique facility ID
     * @param rate Hourly rate to rent facility
     * @param hotelId ID of the hotel the facility is associated with
     * @return HTTP status code
     */
    @RequestMapping("/facilities/create")
    public String createNewFacility(@RequestParam(value = "facilityName") String facilityName,
                                    @RequestParam(value = "description") String description,
                                    @RequestParam(value = "facilityId") long facilityId,
                                    @RequestParam(value = "rate") float rate,
                                    @RequestParam(value = "hotelId") long hotelId){

        String query = "INSERT INTO facilities VALUES(?, ?, ?, ?, ?);";

        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setString(1, facilityName);
            p.setString(2, description);
            p.setLong(3, facilityId);
            p.setFloat(4, rate);
            p.setLong(5, hotelId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Deletes an existing facility
     * @param facilityId ID of the facility being deleted
     * @return HTTP status code
     */
    @RequestMapping("/facilities/delete")
    public String deleteFacility(@RequestParam(value = "facilityId") long facilityId){
        String query = "DELETE FROM facilities WHERE facilityId = " + facilityId;
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
     * Shows details about a specific facility
     * @param facilityId ID of the facility being viewed
     * @return Facility details string
     * @throws SQLException
     */
    @RequestMapping("/facilities/get")
    public String showFacilities(@RequestParam(value = "facilityId") long facilityId) throws SQLException{

        String query = "SELECT * FROM facilities where facilityId =" + facilityId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        if(rs.next()) {
            String facilityName = rs.getString(1);
            String description = rs.getString(2);
            float rentRate = rs.getFloat(4);
            long hotelId = rs.getLong(5);

            sb.append("Facility: ").append(facilityName).append("\nDescription: ").append(description).append("\nFacility ID: ");
            sb.append(facilityId).append("\nRent cost per hour: ").append(rentRate).append("\nHotel ID: ").append(hotelId).append("\n");
            result = sb.toString();
        }
        return result;
    }

    /**
     * List all facilities associated with a certain hotel
     * @param hotelId ID of the hotel from which the facilities are being listed
     * @return String listing the facilities
     * @throws SQLException
     */
    @RequestMapping("/facilities/list")
    public String listFacilities(@RequestParam(value = "hotelId") long hotelId) throws SQLException{
        String query = "SELECT * FROM facilities where hotelId =" + hotelId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        while(rs.next()) {
            long facilityId = rs.getLong(3);
            String facilityName = rs.getString(1);

            sb.append("Facility Name: ").append(facilityName).append("\t\tFacility ID: ").append(facilityId).append("\n");

        }
        result = sb.toString();
        return result;
    }
}
