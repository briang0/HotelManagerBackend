package frontend;

import domain.MaintenanceReq;
import org.springframework.web.client.RestTemplate;
import service.MaintenanceRequestController;

import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.abs;

/**
 * A console for managing maintenance requests.
 * @author Jack Piscitello
 */

public class MaintenanceManagementConsole {
    private final MaintenanceRequestController controller;
    private Scanner scan;

    public MaintenanceManagementConsole(MaintenanceRequestController controller, Scanner scan) {
        this.controller = controller;
        this.scan = scan;
    }

    /**
     * Creates a RestTemplate to be used in other methods in this class and lists menu options for the user
     * @param scan Scanner that is used in all methods in this class
     */
    public static void consoleMenu(Scanner scan){
        RestTemplate restTemplate = new RestTemplate();
        int check = -1;

        while(check != 0) {
            check = -1;
            System.out.println("Select one of the following options:");
            System.out.println("0: Exit");
            System.out.println("1: Create new maintenance request");
            System.out.println("2: Delete an existing maintenance request");
            System.out.println("3: View an existing maintenance request");
            System.out.println("4: Edit an existing maintenance request");
            System.out.println("5: List maintenance requests by status");

            check = scan.nextInt();
            scan.nextLine();

            switch (check) {
                case 0:
                    break;
                case 1:
                    createMaintenanceRequest(scan, restTemplate);
                    break;
                case 2:
                    deleteMaintenanceRequest(scan, restTemplate);
                    break;
                case 3:
                    viewMaintenanceRequest(scan, restTemplate);
                    break;
                case 4:
                    editMaintenanceRequest(scan, restTemplate);
                    break;
                case 5:
                    listMaintenanceRequestsByStatus(scan, restTemplate);
                    break;
                default:
                    System.out.println("Invalid entry. Try again.");
            }
        }
    }

    /**
     * Frontend for creating a new maintenance request
     */
    public static void createMaintenanceRequest(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Maintenance Request ===");
        System.out.println("Enter maintenance request subject:");
        String subject = scan.nextLine();
        System.out.println("Enter maintenance request description:");
        String description = scan.nextLine();
        System.out.println("If known, enter the total cost of repair:");
        float cost = scan.nextFloat();
        scan.nextLine();
        System.out.println("Enter any other comments:");
        String comments = scan.nextLine();

        long id = abs(new Random().nextLong());
        MaintenanceReq.Status status = MaintenanceReq.Status.NEW_REQUEST;

        String ruri = "subject=" + subject + "&description=" + description + "&cost=" + cost + "&reqId="
                + id + "&status=" + status + "&comments=" + comments;

        restTemplate.put("http://localhost:8080/maintenanceRequest/create?" + ruri, String.class);

    }

    /**
     * Method for deleting an existing maintenance request
     */
    public static void deleteMaintenanceRequest(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Delete Existing Maintenance Request ===");
        System.out.println("Enter maintenance request ID:");
        long id  = scan.nextLong();
        scan.nextLine();
        showMaintenanceRequest(id, restTemplate);
        System.out.println("Delete maintenance request shown above?\n0: No\t1: Yes");
        int confirm = scan.nextInt();
        scan.nextLine();
        if(confirm == 1){
            String ruri = "reqId=" + id;

            restTemplate.put("http://localhost:8080/maintenanceRequest/delete?" + ruri, String.class);

            return;
        }
        else
            return;

    }

    /**
     * Method for viewing an existing maintenance request, user provides the request ID
     */
    public static void viewMaintenanceRequest(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== View Maintenance Request ===");
        System.out.println("Enter maintenance request ID:");
        long id = scan.nextLong();
        showMaintenanceRequest(id, restTemplate);
        return;
    }

    /**
     * Method for showing the details of a maintenance request to the user
     * @param id maintenance request ID
     */
    public static void showMaintenanceRequest(long id, RestTemplate restTemplate){
        String vuri = "reqId=" + id;
        String output = restTemplate.getForEntity("http://localhost:8080/maintenanceRequest/get?" + vuri, String.class).getBody();
        System.out.println(output);
        return;
    }

    /**
     * Method for editing an existing maintenance request
     */
    public static void editMaintenanceRequest(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Edit existing maintenance request ===");
        System.out.println("Enter maintenance request ID:");
        long id = scan.nextLong();
        showMaintenanceRequest(id, restTemplate);
        System.out.println("Which field is being changed?\n" +
                        "0: Cancel\n1: Subject\n2: Description\n3: Cost\n4: Status\n5: Comments");

        int check = scan.nextInt();
        String col = "";
        String uri = "";
        scan.nextLine();

        switch(check){
            case 0:
                return;
            case 1:
                System.out.println("Input new subject:");
                String subject = scan.nextLine();
                col = "subject";
                uri = "reqId=" + id + "&col=" + col + "&p=" + subject;
                break;
            case 2:
                System.out.println("Input new description:");
                String desc = scan.nextLine();
                col = "description";
                uri = "reqId=" + id + "&col=" + col + "&p=" + desc;
                break;
            case 3:
                System.out.println("Input new cost:");
                float cost = scan.nextFloat();
                scan.nextLine();
                col = "cost";
                uri = "reqId=" + id + "&col=" + col + "&p=" + cost;
                break;
            case 4:
                System.out.println("Choose new status:\n" +
                        "1: NEW_REQUEST\n2: IN_PROGRESS\n3: ON_HOLD\n4: DECLINED\n5: COMPLETED");
                int status = scan.nextInt();
                scan.nextLine();
                String sString = "";
                switch(status){
                    case 1:
                        sString = "NEW_REQUEST";
                        break;
                    case 2:
                        sString = "IN_PROGRESS";
                        break;
                    case 3:
                        sString = "ON_HOLD";
                        break;
                    case 4:
                        sString = "DECLINED";
                        break;
                    case 5:
                        sString = "COMPLETED";
                        break;
                    default:
                        System.out.println("Invalid entry. Try again.");
                }
                col = "status";
                uri = "reqId=" + id + "&col=" + col + "&p=" + sString;
                break;
            case 5:
                System.out.println("Input new comments section:");
                String comm = scan.nextLine();
                col = "comments";
                uri = "reqId=" + id + "&col=" + col + "&p=" + comm;
                break;
            default:
                System.out.println("Invalid entry. Try again.");
                check = scan.nextInt();
        }
        String url = "http://localhost:8080/maintenanceRequest/edit?" + uri;
        restTemplate.put(url, String.class);

        return;
    }

    public static void listMaintenanceRequestsByStatus(Scanner scan, RestTemplate restTemplate){
        System.out.println("Enter status to list:\n" +
                "1: NEW_REQUEST\n2: IN_PROGRESS\n3: ON_HOLD\n4: DECLINED\n5: COMPLETED");

        String check = scan.nextLine();
        String sString = "";
        switch(check){
            case "1":
                sString = "NEW_REQUEST";
                break;
            case "2":
                sString = "IN_PROGRESS";
                break;
            case "3":
                sString = "ON_HOLD";
                break;
            case "4":
                sString = "DECLINED";
                break;
            case "5":
                sString = "COMPLETED";
                break;
            default:
                System.out.println("Invalid entry. Returning to menu.");

        }
        String uri = "status=" + sString;

        String output = restTemplate.getForEntity("http://localhost:8080/maintenanceRequest/list?" + uri, String.class).getBody();
        System.out.println(output);

        return;

    }

}
