package domain;

import org.springframework.web.client.RestTemplate;

import java.util.Scanner;
import java.util.Date;

/**
 * A class used to hold information regarding a maintenance request
 *
 * @author Jack Piscitello
 */

public class MaintenanceReq {
    public enum Status{COMPLETED, IN_PROGRESS, NEW_REQUEST, ON_HOLD, DECLINED};

    private String subject;
    private String description;
    private Date time;
    private float cost;
    private int id;
    private Status status;
    private String comments;

    public MaintenanceReq(String subject, String description, float cost, String comments, int id){
        this.subject = subject;
        this.description = description;
        this.time = new Date();
        this.cost = cost;
        this.id = id;
        this.status = Status.NEW_REQUEST;
        this.comments = comments;
    }

    public Date getTime() {return time;}
    public float getCost() {return cost;}
    public int getId() {return id;}
    public Status getStatus() {return status;}
    public String getComments() {return comments;}
    public String getDescription() {return description;}
    public String getSubject() {return subject;}
}

