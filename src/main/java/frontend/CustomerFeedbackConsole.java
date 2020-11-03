package frontend;

import domain.people.customer.Customer;
import org.springframework.web.client.RestTemplate;
import service.CustomerFeedbackController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

/**
 * A console to manage customer feedback methods
 * @author Jack Piscitello
 */

public class CustomerFeedbackConsole {
    private Scanner scan;
    private CustomerFeedbackController controller;

    public void customerFeedbackMenu(Scanner scan){
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("=== Customer Feedback Menu ===");
        System.out.println("1: Create new customer review");
        System.out.println("2: Create new customer complaint");
        System.out.println("3: Display reviews");
        System.out.println("4: Edit existing customer complaint");

        switch(scan.nextInt()){
            case 1:
                createReview(scan, restTemplate);
                return;
            case 2:
                createComplaint(scan, restTemplate);
                return;
            case 3:
                displayReviews(scan, restTemplate);
                return;
            case 4:
                editComplaint(scan, restTemplate);
                return;
            default:
                System.out.println("Invalid input");
                return;
        }
    }

    public static void createReview(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Review ===");
        System.out.println("Will this review be anonymous?\nY or N:");
        boolean check = false;
        while(check == false) {
            String anon = scan.nextLine();
            if (anon == "Y") {
                Customer customer = null;
                check = true;
            } else if (anon == "N") {
                System.out.println("Enter customer ID:");
                long custID = scan.nextLong();

                //get customer from database TODO

                check = true;
            }
            else
                System.out.println("Invalid input, try again.");
        }

        System.out.println("How many stars(0-5) would you give your experience?");
        int rating = scan.nextInt();
        System.out.println("Please describe your experience below:");
        String review = scan.nextLine();


        //TODO
    }

    public static void createComplaint(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== New Customer Complaint ===");
        System.out.println("Enter customer ID:");
        long custId = scan.nextLong();
        System.out.println("Enter employee ID:");
        long empId = scan.nextLong();
        System.out.println("Enter complaint details:");
        String complaintDetails = scan.nextLine();
        System.out.println("Enter refund amount, enter 0 if no refund:");
        float refund = scan.nextFloat();

        //TODO
    }

    public static void editComplaint(Scanner scan, RestTemplate restTemplate){
        //TODO
    }

    public static void displayReviews(Scanner scan, RestTemplate restTemplate){
        //TODO
    }

}
