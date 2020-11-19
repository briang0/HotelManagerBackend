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
import java.util.Random;
import java.util.Scanner;

/**
 * A POJO that represents a room listing
 * @author Brian Guidarini
 */
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
    private long rateId;

    /**
     * Constructor
     * @param description
     * A description of the hotel room that is meant to convince someone to rent it
     * @param cost
     * The cost of the room
     * @param frequency
     * The frequency of payments
     * @param roomDescription
     * The description of the room itself, such as suite or standard or 1 bed
     * @param hotelAddress
     * The address of the hotel the room is in
     * @param phone
     * The phone number for the hotel
     * @param hotelId
     * The hotelId of the hotel
     * @param roomId
     * The roomId of the the room represented in the listing
     */
    public ListingContainer(String description, float cost, int frequency, String roomDescription,
                            String hotelAddress, String phone, long hotelId, long roomId, long rateId) {
        this.description = description;
        this.cost = cost;
        this.frequency = frequency;
        this.roomDescription = roomDescription;
        this.hotelAddress = hotelAddress;
        this.phone = phone;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.rateId = rateId;
    }

    /**
     * Loads a bufferedImage representing this instance of the room from the database
     * @throws SQLException
     * @throws IOException
     */
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

    /**
     * Pulls up a JFrame to show the room image to the potential customer
     */
    public void viewImage() {
        if (image != null) {
            JFrame frame = new JFrame();
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(new JLabel(new ImageIcon(image)));
            frame.pack();
            frame.setVisible(true);
        } else {
            System.out.println("No image available for this listing.");
        }
    }

    /**
     * Books the advertised room or a room that is the same as the advertised room
     * @param scan
     * The scanner to take in user input
     */
    public void book(Scanner scan) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("When are you checking in? (YYYY-MM-DDxHH:MM:SS)");
        String checkInResponse = scan.next();
        System.out.println("When are you checking out? (YYYY-MM-DDxHH:MM:SS)");
        String checkOutResponse = scan.next();
        checkInResponse = checkInResponse.replace("x", " ");
        checkOutResponse = checkOutResponse.replace("x", " ");

        System.out.println("Checking availability....");
        String url = "http://localhost:8080/listing/getUnavailableRooms?hotelId=" + hotelId + "&roomDescription=" +
                roomDescription + "&checkInTime=" + checkInResponse + "&checkOutTime=" + checkOutResponse;
        String response = restTemplate.getForObject(url, String.class);
        String[] arr = response.split("\n");
        int numRooms = Integer.parseInt(arr[0]);
        int numUnavailable = arr.length - 1;
        System.out.println("There are " + (numRooms - numUnavailable) + " rooms available!");
        System.out.println("What is your first name? ");
        String first = scan.next();
        System.out.println("What is your last name? ");
        String last = scan.next();
        System.out.println("What is your date of birth? YYYY-MM-DD");
        String dob = scan.next();
        url = "http://localhost:8080/customer/exists?firstName=" + first + "&lastName=" + last + "&dob=" + dob;
        String out = restTemplate.getForObject(url, String.class);
        long custId;
        try {
            custId = Long.parseLong(out);
        } catch (Exception e) {
            custId = Math.abs(new Random().nextLong());
        }

        long reservationId = Math.abs(new Random().nextLong());

        url = "http://localhost:8080/listing/getRooms?hotelId=" + hotelId + "&roomDescription=" + roomDescription;

        String response2 =restTemplate.getForObject(url, String.class);
        String [] rooms = response2.split("\n");
        int count = 0;
        while (true) {
            boolean collision = false;
            for (int i = 0; i < arr.length;i++) {
                if (arr[i].split(",")[0].equals(rooms[count].split(" ")[0])) {
                    collision = true;
                    break;
                }
            }
            if (!collision) {
                break;
            }
            count++;
        }

        url = "http://localhost:8080/reservation/create?checkInDate=" + checkInResponse + "&checkOutDate=" +
                checkOutResponse + "&reservationId=" + reservationId + "&customerId=" + custId +
                "&rateId=" + rateId + "&billId=0" + "&roomId=" + rooms[count].split(" ")[1];
        restTemplate.getForObject(url, String.class);
        System.out.println("Room " + rooms[count].split(" ")[0] + " was booked!");
        System.exit(0);
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

    public long getHotelId() {
        return hotelId;
    }

    public long getRoomId() {
        return roomId;
    }

    public String toString() {
        return "Location: " + hotelAddress  + " phone: " + phone + "\nRoom Type: " + roomDescription + " $" + cost + "per " + frequency + "day(s)" + "\n\n" + description;
    }
}
