package domain;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Mail {
    private int messageID;
    private long timestamp;
    private boolean read;
    private int senderID;
    private String subject;
    private String message;

    private static final Gson GSON = new Gson();

    public static class Message {
        int sender;
        List<Integer> recipients;
        String subject;
        String message;

        public Message(int sender, List<Integer> recipients, String subject, String message) {
            this.sender = sender;
            this.recipients = recipients;
            this.subject = subject;
            this.message = message;
        }

        public String getSubject() {
            return subject;
        }

        public int getSender() {
            return sender;
        }

        public List<Integer> getRecipients() {
            return recipients;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum ReadStatus {
        UNREAD,
        READ
    };

    public Mail(int messageID, long timestamp, boolean read, int senderID, String subject, String message) {
        this.messageID = messageID;
        this.timestamp = timestamp;
        this.read = read;
        this.senderID = senderID;
        this.subject = subject;
        this.message = message;
    }

    public static boolean send(int sender, String subject, String message, List<Integer> recipients) {
        RestTemplate request = new RestTemplate();
        try {
            request.postForEntity(new URI("http://localhost:8080/mail/send"), new Message(sender, recipients, subject, message), String.class);
        } catch (URISyntaxException e) {}

        return true;
    }

    public static LinkedList<Mail> getInbox(int employeeID) {
        UriComponents uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/mail/inbox")
                .queryParam("employeeID", employeeID)
                .build();

        RestTemplate request = new RestTemplate();
        String resp = request.getForEntity(uri.toUri(), String.class).getBody();
        return new LinkedList<>(Arrays.asList(GSON.fromJson(resp, Mail[].class)));
    }

    public int getMessageID() {
        return messageID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public int getSenderID() {
        return senderID;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }
}
