package service;

import db.Connector;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;

@RestController
public class Controller {

    Connection jdbc;

    //http://localhost:8080/testPhrase/?phrase="Hello"
    @RequestMapping("/testPhrase")
    public String testWithParam(@RequestParam(value = "phrase") String phrase) {
        return "Phrase received: " + phrase;
    }

    //http://localhost:8080/test
    @RequestMapping("/test")
    public String getCode() {
        return "Hit the endpoint";
    }

    @RequestMapping("/makeReservation")
    public String getCode(@RequestParam(value = "checkInDate") String checkIn,
                       @RequestParam(value = "checkOutDate") String checkOut,
                       @RequestParam(value = "reservationId") long reservationId,
                       @RequestParam(value = "customerId") long customerId,
                       @RequestParam(value = "rateId") long rateId,
                       @RequestParam(value = "billId") long billId) throws ParseException {
        System.out.println(checkIn + "\n" + checkOut + "\n" + reservationId + "\n" + customerId + "\n" + rateId + "\n" + billId);
        Date date1 = DateUtils.parseDate(checkIn,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        Date date2 = DateUtils.parseDate(checkOut,
                "yyyy-MM-dd HH:mm:ss", "dd/MM-yyyy");
        java.sql.Date sqld1 = new java.sql.Date(date1.getTime());
        java.sql.Date sqld2 = new java.sql.Date(date2.getTime());

        String query = "INSERT INTO reservation VALUES(?, ?, ?, ?, ?, ?);";
        try {
            jdbc = Connector.getConnection("brian", "YuckyP@ssw0rd");
            System.out.println(jdbc == null);
            PreparedStatement p = jdbc.prepareStatement(query);
            p.setLong(1, customerId);
            p.setLong(2, billId);
            p.setLong(3, reservationId);
            p.setDate(4, sqld1);
            p.setDate(5, sqld2);
            p.setLong(6, rateId);
            p.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return  "{" +
                    "status: 401" +
                    "}";
        }
        return  "{" +
                "status: 200" +
                "}";

    }

}
