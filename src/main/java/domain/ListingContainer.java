package domain;

import db.Connector;
import domain.room.Reservation;
import domain.room.Room;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

public class ListingContainer {

    private String description;
    private BufferedImage image;
    private float cost;
    private int frequency;
    private String roomDescription;
    private String hotelAddress;
    private String phone;
    private long hotelId;
    private long roomId;

    public ListingContainer(String description, float cost, int frequency, String roomDescription,
                            String hotelAddress, String phone, long hotelId, long roomId) {
        this.description = description;
        this.cost = cost;
        this.frequency = frequency;
        this.roomDescription = roomDescription;
        this.hotelAddress = hotelAddress;
        this.phone = phone;
        this.hotelId = hotelId;
        this.roomId = roomId;
    }

    public void loadBufferedImageFromDb() throws SQLException, IOException {
        String query = "SELECT picture FROM listing WHERE hotelId = " + hotelId + " AND roomId = " + roomId + " AND description = '" + description + "';";
        Connection jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
        Statement stmt = jdbc.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        Blob imageBlob = rs.getBlob(1);
        InputStream binaryStream = imageBlob.getBinaryStream(1, imageBlob.length());
        BufferedImage bi = ImageIO.read(binaryStream);
        image = bi;
    }

    public void viewImage() {
        if (image != null) {
            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(new JLabel(new ImageIcon(image)));
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            System.out.println("No image available for this listing.");
        }
    }

    public void book(Scanner scan) {
        RestTemplate restTemplate = new RestTemplate();
        Reservation.createReservation(scan, restTemplate);
    }

    public String getDescription() {
        return description;
    }

    public BufferedImage getImage() {
        return image;
    }

    public float getCost() {
        return cost;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String toString() {
        return "Location: " + hotelAddress  + " phone: " + phone + "\nRoom Type: " + roomDescription + " $" + cost + "per " + frequency + "day(s)" + "\n\n" + description;
    }
}
