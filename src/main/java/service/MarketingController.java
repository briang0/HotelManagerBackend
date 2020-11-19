package service;

import db.Connector;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;

/**
 * The controller for market listings
 * @author Brian Guidarini
 */
@RestController
public class MarketingController {

    Connection jdbc;

    /**
     * This endpoint creates a merket listing
     * @param hotelId
     * The id of the hotel the listing is in
     * @param roomId
     * The room id associated with a single room of its type
     * @param imageDir
     * The directory to upload the image from
     * @param description
     * The description of the room that is meant to sell the room
     * @return
     * Returns 200 on success, 400 on failure
     * @throws IOException
     */
    @RequestMapping("/listing/create")
    public String createListing(@RequestParam(value = "hotelId") long hotelId,
                                     @RequestParam(value = "roomId") long roomId,
                                     @RequestParam(value = "imageDir") String imageDir,
                                     @RequestParam(value = "description") String description) throws IOException {
        String query = "INSERT INTO listing VALUES(?, ?, ?, ?);";
        File image = new File(imageDir);
        InputStream inputStream = new FileInputStream(image);


        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, hotelId);
            p.setLong(2, roomId);
            p.setString(3, description);
            p.setBinaryStream(4, inputStream, (int)(image.length()));
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }

        return "{\nstatus: 200\n}";
    }

    /**
     * An endpoint to get all listings matching the entered criteria
     * @param key
     * The keyword or phrase based on location to find a listing in an area
     * @return
     * Returns a string of all listing data
     * @throws SQLException
     * @throws IOException
     */
    @RequestMapping("/listing/getAll")
    public String getAllListings(@RequestParam(value="key") String key) throws SQLException, IOException {
        String query = "SELECT listing.description, listing.picture, rate.cost,\n" +
                " rate.payPeriod, room.roomDescription, hotel.address, hotel.phone, listing.hotelId, listing.roomId, rate.rateId FROM listing\n" +
                "INNER JOIN hotel ON listing.hotelId = hotel.hotelId\n" +
                "INNER JOIN room ON room.roomId = listing.roomId\n" +
                "INNER JOIN rate ON rate.rateId = room.rateId\n" +
                "WHERE address LIKE '%" + key + "%'";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String output = "";
        StringBuilder sb = new StringBuilder(output);
        while (rs.next()) {
            String description = rs.getString(1);
            float cost = rs.getFloat(3);
            int payPeriod = rs.getInt(4);
            String roomDescription = rs.getString(5);
            String address = rs.getString(6);
            String phone = rs.getString(7);
            long hotelId = rs.getLong(8);
            long roomId = rs.getLong(9);
            long rateId = rs.getLong(10);

            sb.append(description).append("\n").append(cost).append("\n").append(payPeriod).append("\n")
                    .append(roomDescription).append("\n").append(address).append("\n").append(phone)
                    .append("\n").append(hotelId).append("\n").append(roomId).append("\n").append(rateId).append("\n");
        }
        return sb.toString();
    }

    @RequestMapping("/listing/getUnavailableRooms")
    public String getUnavailableRooms(@RequestParam(value = "hotelId") long hotelId,
                                      @RequestParam(value = "roomDescription") String roomDescription,
                                      @RequestParam(value = "checkInTime") String checkInTime,
                                      @RequestParam(value = "checkOutTime") String checkOutTime) throws SQLException, ParseException {
        java.util.Date date1 = DateUtils.parseDate(checkInTime,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        Date date2 = DateUtils.parseDate(checkOutTime,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        String query =" SELECT room.roomNumber, room.roomId FROM room " +
                "JOIN reservation on reservation.roomId = room.roomId " +
                "WHERE room.hotelId = ? AND room.roomDescription = " +
                "? AND ( reservation.checkOut >= ? AND reservation.checkIn <= ?)";
        String query2 = "SELECT * FROM room WHERE hotelId = ? AND roomDescription = ?";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        StringBuilder sb = new StringBuilder("");
        PreparedStatement stmt = jdbc.prepareCall(query2);
        stmt.setLong(1, hotelId);
        stmt.setString(2, roomDescription);
        ResultSet rs = stmt.executeQuery();
        int count = 0;
        while(rs.next()) {
            count++;
        }
        sb.append(count).append("\n");
        stmt = jdbc.prepareCall(query);
        stmt.setLong(1, hotelId);
        stmt.setString(2, roomDescription);
        stmt.setDate(3, new java.sql.Date(date1.getTime()));
        stmt.setDate(4, new java.sql.Date(date2.getTime()));
        rs = stmt.executeQuery();
        while (rs.next()) {
            int roomNumber = rs.getInt(1);
            long roomId = rs.getLong(2);
            sb.append(roomNumber).append(",").append(roomId).append("\n");
        }
        return sb.toString();
    }

    @RequestMapping("/listing/getRooms")
    public String getRooms(@RequestParam(value = "hotelId") long hotelId,
                           @RequestParam(value = "roomDescription") String roomDescription) throws SQLException {
        String query = "SELECT roomNumber, roomId FROM room WHERE hotelId = " + hotelId + " AND " + "roomDescription = '" + roomDescription + "'";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        assert jdbc != null;
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String output = "";
        StringBuilder sb = new StringBuilder(output);
        while (rs.next()) {
            int roomNumber = rs.getInt(1);
            long roomId = rs.getLong(2);
            sb.append(roomNumber).append(" ").append(roomId).append("\n");
        }
        return sb.toString();
    }

}
