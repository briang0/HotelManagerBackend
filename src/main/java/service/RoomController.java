package service;

import db.Connector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class RoomController {

    Connection jdbc;

    /**
     * Endpoint to create a room in the database
     * @param roomNumber The room number within the hotel
     * @param roomId The unique id that represents the room
     * @param houseKeepingStatus False if no one is currently housekeeping, true otherwise
     * @param roomDescription A description of the room. Such as suite, room, or penthouse
     * @param rateId The id of the rate for this room
     * @param hotelId The id of the hotel in which this room resides
     * @return Returns the http status
     */
    @RequestMapping("/room/create")
    public String createRoom(@RequestParam(value = "roomNumber") int roomNumber,
                             @RequestParam(value = "roomId") long roomId,
                             @RequestParam(value = "houseKeepingStatus") boolean houseKeepingStatus,
                             @RequestParam(value = "roomDescription") String roomDescription,
                             @RequestParam(value = "rateId") long rateId,
                             @RequestParam(value = "hotelId") long hotelId) {
        String query = "INSERT INTO room VALUES(?, ?, ?, ?, ?, ?, ?);";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            assert jdbc != null;
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setDate(1, null);
            p.setInt(2, roomNumber);
            p.setLong(3, roomId);
            p.setBoolean(4, houseKeepingStatus);
            p.setString(5, roomDescription);
            p.setLong(6, rateId);
            p.setLong(7, hotelId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return "{\nstatus: 400\n}";
        }

        return "{\nstatus: 200\n}";
    }

}
