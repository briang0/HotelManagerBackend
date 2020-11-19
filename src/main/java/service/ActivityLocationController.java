package service;

import db.Connector;
import org.hibernate.hql.internal.ast.SqlASTFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * Controller to connect to the database controlling changes relating to activityLocations
 * @author Jack Piscitello
 */

@RestController
public class ActivityLocationController {
    Connection jdbc;

    /**
     * Creates new activity location
     * @param name Location name
     * @param address Location address
     * @param partySize Max party size for the location
     * @param details Details of the location
     * @param distance Distance from the hotel
     * @param hotelId ID of the hotel
     * @return HTTP Status code
     */
    @RequestMapping("activityLocation/addLocation")
    public String addLocation(@RequestParam(value = "name") String name,
                              @RequestParam(value = "address") String address,
                              @RequestParam(value = "partySize") int partySize,
                              @RequestParam(value = "details") String details,
                              @RequestParam(value = "distance") float distance,
                              @RequestParam(value = "hotelId") long hotelId){
        String query = "INSERT INTO activityLocation VALUES(?, ?, ?, ?, ?, ?, ?);";

        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setString(1, name);
            p.setString(2, address);
            p.setInt(3, partySize);
            p.setString(4, details);
            p.setFloat(5, distance);
            p.setLong(6, hotelId);
            p.setInt(7, 0);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Lists locations
     * @param hotelId ID of the hotel
     * @return List of locations
     * @throws SQLException
     */
    @RequestMapping("/activityLocation/listLocations")
    public String listLocations(@RequestParam(value = "hotelId") long hotelId) throws SQLException {
        String query = "SELECT * FROM activityLocation WHERE hotelId=" + hotelId + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);

        while(rs.next()) {
            int locationId = rs.getInt(7);
            String locationName = rs.getString(1);
            float distance = rs.getFloat(5);
            String details = rs.getString(4);
            String address = rs.getString(2);
            int partySize = rs.getInt(3);

            sb.append("Location Name(ID): ").append(locationName).append("(").append(locationId).append(")\t\tDistance from hotel: ").append(distance);
            sb.append(" mi\t\tMax Party Size: ").append(partySize).append("\t\tAddress: ").append(address).append("\n").append(details).append("\n\n");

        }
        result = sb.toString();
        return result;
    }

    /**
     * Deletes a location
     * @param locationId
     * @return
     */
    @RequestMapping("/activityLocation/deleteLocation")
    public String deleteLocation(@RequestParam(value = "locationId") int locationId){
        String query = "DELETE FROM activityLocation WHERE locationId="+locationId + ";";
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
     * Gets the location's distance from the hotel
     * @param locationId
     * @return
     * @throws SQLException
     */
    @RequestMapping("/activityLocation/getLocationDistance")
    public String getLocationDistance(@RequestParam(value = "locationId") int locationId)throws SQLException{
        String query = "SELECT * FROM activityLocation WHERE locationId=" + locationId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";

        if(rs.next()) {
            result = Float.toString(rs.getFloat(5));
        }
        return result;
    }

    /**
     * Gets location's max party size
     * @param locationId
     * @return
     * @throws SQLException
     */
    @RequestMapping("/activityLocation/getMaxPartySize")
    public String getMaxPartySize(@RequestParam(value = "locationId") int locationId)throws SQLException{
        String query = "SELECT * FROM activityLocation WHERE locationId=" + locationId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";

        if(rs.next()) {
            result = Integer.toString(rs.getInt(3));
        }
        return result;
    }
}
