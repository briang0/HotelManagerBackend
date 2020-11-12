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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public String getAllListings(@RequestParam(value="key") String key) {
        String query = "SELECT listing.* from listing\n" +
                "INNER JOIN hotel ON listing.hotelId = hotel.hotelId\n" +
                " WHERE address LIKE '%" + key + "%'";

        return "";
    }
}
