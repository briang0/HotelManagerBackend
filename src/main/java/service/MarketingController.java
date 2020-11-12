package service;

import db.Connector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

@RestController
public class MarketingController {

    Connection jdbc;

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

    @RequestMapping("/listing/getAll")
    public String getAllListings(@RequestParam(value="key") String key) throws SQLException, IOException {
        String query = "SELECT listing.description, listing.picture, rate.cost,\n" +
                " rate.payPeriod, room.roomDescription, hotel.address, hotel.phone, listing.hotelId, listing.roomId FROM listing\n" +
                "INNER JOIN hotel ON listing.hotelId = hotel.hotelId\n" +
                "INNER JOIN room ON room.roomId = listing.roomId\n" +
                "INNER JOIN rate ON rate.rateId = room.rateId\n" +
                "WHERE address LIKE '%" + key + "%'";
        jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
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
            sb.append(description).append("\n").append(cost).append("\n").append(payPeriod).append("\n")
                    .append(roomDescription).append("\n").append(address).append("\n").append(phone)
                    .append("\n").append(hotelId).append("\n").append(roomId).append("\n");
        }
        return sb.toString();
    }
}
