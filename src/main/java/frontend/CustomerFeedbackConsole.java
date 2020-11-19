package frontend;

import domain.CustomerComplaint;
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

    public static void customerFeedbackMenu(Scanner scan){
        RestTemplate restTemplate = new RestTemplate();
        int check = -1;
        while(true) {
            System.out.println("=== Customer Feedback Menu ===");
            System.out.println("0: Exit");
            System.out.println("1: Create new customer review");
            System.out.println("2: Create new customer complaint");
            System.out.println("3: Display reviews");
            System.out.println("4: Display customer complaints by status");
            System.out.println("5: Edit existing customer complaint");
            System.out.println("6: Settle customer complaint");
            check = scan.nextInt();
            switch (check) {
                case 0:
                    return;
                case 1:
                    createReview(scan, restTemplate);
                    break;
                case 2:
                    createComplaint(scan, restTemplate);
                    break;
                case 3:
                    displayReviews(scan, restTemplate);
                    break;
                case 4:
                    displayComplaints(scan, restTemplate);
                    break;
                case 5:
                    editComplaint(scan, restTemplate);
                    break;
                case 6:
                    settleComplaint(scan, restTemplate);
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }

    /**
     * Creates a review
     * @param scan Scanner to take in values
     * @param restTemplate RestTemplate to connect to endpoint
     *                     These parameters are constant in all other methods below this
     */
    public static void createReview(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Review ===");
        System.out.println("Will this review be anonymous?\n1: Yes\n2: No");
        boolean check = false;
        String name = "";
        while(check == false) {
            int anon = scan.nextInt();
            scan.nextLine();
            if (anon == 1) {
                name = "Anonymous";
                check = true;
            } else if (anon == 2) {
                System.out.println("Enter customer ID(enter 0 if unknown):");
                long custID = scan.nextLong();
                scan.nextLine();
                name = Customer.getCustomerName(custID, restTemplate);
                if(name == null){
                    System.out.println("No customer associated with ID, enter customer name:");
                    name = scan.nextLine();
                }
                check = true;
            }
            else
                System.out.println("Invalid input, try again.");
        }
        System.out.println("How many stars(0-5) would you give your experience?");
        int rating = scan.nextInt();
        scan.nextLine();
        while(rating > 5 || rating < 0){
            System.out.println("Invalid rating entered. Try again.");
            rating = scan.nextInt();
            scan.nextLine();
        }
        System.out.println("Please describe your experience below:");
        String review = scan.nextLine();

        String uri = "name=" + name + "&rating=" + rating + "&review=" + review;
        String url = "http://localhost:8080/customerFeedback/createReview?" + uri;

        restTemplate.put(url, String.class);

        System.out.println("Review received, thank you for your input.\n");
    }

    public static void createComplaint(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== New Customer Complaint ===");
        System.out.println("Enter customer ID:");
        long custId = scan.nextLong();
        System.out.println("Enter employee ID:");
        long empId = scan.nextLong();
        scan.nextLine();
        System.out.println("Enter complaint details:");
        String complaintDetails = scan.nextLine();
        System.out.println("Enter refund amount, enter 0 if no refund:");
        float refund = scan.nextFloat();
        scan.nextLine();
        String customerName = Customer.getCustomerName(custId, restTemplate);


        String uri = "customerName=" + customerName + "&customerId=" + custId + "&employeeId=" + empId + "&complaint=" + complaintDetails + "&refund=" + refund;
        String url = "http://localhost:8080/customerFeedback/createComplaint?" + uri;

        restTemplate.put(url, String.class);
    }

    public static void editComplaint(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Edit Existing Complaint ===");
        System.out.println("Enter comlaint ID:");
        Long complaintId = scan.nextLong();
        scan.nextLine();
        int check = -1;
        System.out.println("Which field is being changed?\n" +
                "0: Exit\n" +
                "1: Employee ID\n" +
                "2: Complaint Details\n" +
                "3: Complaint Status\n" +
                "4: Refund Amount");
        String param = "";
        String col = "";
        String uri = "";
        check = scan.nextInt();
        scan.nextLine();
        switch (check){
            case 0:
                return;
            case 1:
                System.out.println("Enter new employee ID:");
                param = String.valueOf(scan.nextLong());
                scan.nextLine();
                col = "employeeId";
                uri = "complaintId=" + complaintId + "&col=" + col + "&param=" + param;
                break;
            case 2:
                System.out.println("Enter new complaint details:");
                param = scan.nextLine();
                col = "complaint";
                uri = "complaintId=" + complaintId + "&col=" + col + "&param=" + param;
                break;
            case 3:
                System.out.println("Choose new status:\n" +
                        "1: NEW_COMPLAINT\n2: ACTIVE\n3: CLOSED");
                int status = scan.nextInt();
                scan.nextLine();
                switch(status){
                    case 1:
                        param = "NEW_REQUEST";
                        break;
                    case 2:
                        param = "ACTIVE";
                        break;
                    case 3:
                        param = "CLOSED";
                        break;
                    default:
                        System.out.println("Invalid entry. Try again.");
                        status = scan.nextInt();
                        scan.nextLine();
                }
                col = "complaintStatus";
                uri = "complaintId=" + complaintId + "&col=" + col + "&param=" + param;
                break;
            case 4:
                System.out.println("Enter new employee ID:");
                param = String.valueOf(scan.nextFloat());
                scan.nextLine();
                col = "refund";
                uri = "complaintId=" + complaintId + "&col=" + col + "&param=" + param;
                break;
            default:
                System.out.println("Invalid entry. Try again.");
                check = scan.nextInt();
        }

        String url = "http://localhost:8080/customerFeedback/editComplaint?" + uri;
        restTemplate.put(url, String.class);
    }

    public static void displayComplaints(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== List Complaints By Status ===");
        System.out.println("Choose a status:\n0: Exit\n1: New Complaint\n2: Active\n3: Closed");
        int status = scan.nextInt();
        scan.nextLine();
        String param = "";
        switch (status){
            case 0:
                return;
            case 1:
                param = "NEW_COMPLAINT";
                break;
            case 2:
                param = "ACTIVE";
                break;
            case 3:
                param = "CLOSED";
                break;
            default:
                System.out.println("Invalid Entry. Try again.");
                status = scan.nextInt();
                scan.nextLine();
        }

        String uri = "complaintStatus=" + param;
        String url = "http://localhost:8080/customerFeedback/viewComplaints?" + uri;

        //System.out.println(url);

        String output = restTemplate.getForObject(url, String.class);
        System.out.println(output);
    }

    public static void displayReviews(Scanner scan, RestTemplate restTemplate){
        System.out.println("Enter page number:");
        int page = scan.nextInt();
        scan.nextLine();

        String url = "http://localhost:8080/customerFeedback/displayReviews?pageNumber=" + page;
        //System.out.println(url);
        String output = restTemplate.getForObject(url, String.class);

        System.out.println(output);
    }

    public static void  settleComplaint(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Close Complaint and Apply Credit to Customer ===");
        System.out.println("Enter complaint ID:");
        long complaintId = scan.nextLong();
        scan.nextLine();

        String url1 = "http://localhost:8080/customerFeedback/showSettleInfo?complaintId="+complaintId;
        String output = restTemplate.getForObject(url1, String.class);
        System.out.println(output);

        System.out.println("Apply this credit to the customer's account?\n1: Yes\n2: No");
        int check = scan.nextInt();
        scan.nextLine();
        switch(check){
            case 1:
                break;
            case 2:
                System.out.println("Settle complaint cancelled.");
                return;
            default:
                System.out.println("Invalid entry. Try again.");
                check = scan.nextInt();
                scan.nextLine();
        }

        String url2 = "http://localhost:8080/customerFeedback/getCustomerId?complaintId=" + complaintId;
        String customerId = restTemplate.getForObject(url2, String.class);

        String url3 = "http://localhost:8080/customerFeedback/getRefund?complaintId=" + complaintId;
        String refund = restTemplate.getForObject(url3, String.class);

        String url4 = "http://localhost:8080/customer/getCredit?customerId="+customerId;
        String credit = restTemplate.getForObject(url4, String.class);

        String uri5 = "complaintId=" + complaintId + "&customerId=" + customerId + "&refund=" + refund + "&credit=" + credit;
        String url5 = "http://localhost:8080/customerFeedback/settleComplaint?" + uri5;
        //System.out.println(url5);
        restTemplate.put(url5, String.class);
    }

}
