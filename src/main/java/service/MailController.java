package service;

import com.google.gson.Gson;
import db.Connector;
import domain.Employee;
import domain.Inventory;
import domain.Mail;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@RestController
public class MailController {
    private static final Gson GSON = new Gson();
    private Connection db;

    public MailController() {
        db = Connector.getConnection("brian", "YuckyP@ssw0rd");
    }

    @RequestMapping("/mail/create")
    public String createInbox(@RequestParam int employeeID) {
        try (Statement stmt = db.createStatement()) {
            stmt.executeUpdate(String.format("create table mailbox_%d (msg_id int auto_increment key, timestamp long, read_status int, sender int, subject varchar(128), message varchar(2048));",
                    employeeID));
            return "ok";
        } catch (SQLException e) {
            return "error";
        }
    }

    @RequestMapping("/mail/inbox")
    public String getInbox(@RequestParam int employeeID) {
        try (Statement stmt = db.createStatement()) {
            LinkedList<Mail> inbox = new LinkedList<>();
            try (ResultSet result = stmt.executeQuery(String.format("select * from mailbox_%d order by timestamp desc;", employeeID))) {
                while (result.next()) {
                    Mail mail = new Mail(result.getInt(1), result.getLong(2), result.getInt(3) == 1,
                            result.getInt(4), result.getString(5), result.getString(6));
                    inbox.add(mail);
                }
            }

            return GSON.toJson(inbox.toArray(), Mail[].class);
        } catch (SQLException e) {
            return "error";
        }
    }

    @RequestMapping("/mail/send")
    public String send(@RequestBody Mail.Message message) {
        try {
            for (Integer recipient : message.getRecipients()) {
                try (Statement stmt = db.createStatement()) {
                    stmt.executeUpdate(String.format("insert into mailbox_%d(timestamp, read_status, sender, subject, message) values(%d,%d,%d,'%s','%s');",
                            recipient, Instant.now().getEpochSecond(), Mail.ReadStatus.UNREAD.ordinal(), message.getSender(),
                            message.getSubject(), message.getMessage()));
                }
            }
        } catch (SQLException e) {
            return "error";
        }
        return "ok";
    }
}
