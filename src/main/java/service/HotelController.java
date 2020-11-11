package service;

import db.Connector;
import domain.Inventory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * A controller to handle hotels
 * @author Brian Guidarini
 */
@RestController
public class HotelController {

    Connection jdbc;

    /**
     * Creates a hotel in the database
     * @param hotelId The unique id that describes the hotel
     * @param address The address of the hotel
     * @return Returns the http status of the operation
     */
    @RequestMapping("/hotel/create")
    public String createHotel(@RequestParam(value = "hotelId") long hotelId,
                              @RequestParam(value = "address") String address) {

        String query = "INSERT INTO hotel VALUES(?, ?);";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, hotelId);
            p.setString(2, address);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }
        return "{\nstatus: 200\n}";
    }

    /**
     * Gets all rooms for a given hotel room
     * @param hotelId The unique id of the hotel
     * @return Returns the http status code
     * @throws SQLException
     */
    @RequestMapping("/hotel/get")
    public String getHotelRooms(@RequestParam(value = "hotelId") long hotelId) throws SQLException {
        String query = "SELECT * FROM room WHERE hotelId = " + hotelId;
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            int roomNumber = rs.getInt(2);
            long roomId = rs.getLong(3);
            boolean housekeepingStatus = rs.getBoolean(4);
            String roomDescription = rs.getString(5);
            long rateId = rs.getLong(6);
            sb.append("RoomNumber: ").append(roomNumber).append(" roomId: ").append(roomId).append(" House Keeping Status: ").append(housekeepingStatus).append(" Description: ").append(roomDescription).append(" rateId: ").append(rateId).append("\n");
        }
        result = sb.toString();
        return result;
    }

    /**
     * Retrieves all hotels in the database
     * @return
     * A String containing the data for all hotels
     * @throws SQLException
     */
    @RequestMapping("/hotel/getAllHotels")
    public String getAllHotels() throws SQLException {
        String query = "SELECT * FROM hotel";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String result = "";
        StringBuilder sb = new StringBuilder(result);
        while(rs.next()) {
            long hotelId = rs.getLong(1);
            String address = rs.getString(2);
            String hotelName = rs.getString(3);
            int hotelIndex = rs.getInt(4);
            sb.append(hotelIndex).append(")\tHotel Name: ").append(hotelName).append("\tAddress: ").append(address);
            sb.append("\tHotel ID: ").append(hotelId).append("\n");
        }
        result = sb.toString();
        return result;
    }

    @RequestMapping("/hotel/getId")
    public String getId(@RequestParam(value = "hotelIndex") int hotelIndex) throws SQLException{
        String query = "SELECT * FROM hotel.hotel WHERE hotelIndex=" + hotelIndex + ";";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if(rs.next()) {
            String output = Long.toString(rs.getLong(1));
            return output;
        }
        return "0";
    }
}
