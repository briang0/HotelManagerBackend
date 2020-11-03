package frontend;

import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Scanner;

/**
 * Console for running methods pertaining to facility management
 * @author Jack Piscitello
 */

public class FacilityManagementConsole {

    /**
     * A main menu for the facility management console that calls other methods
     * @param scan A scanner to take user inputs
     */
    public static void facilityManagementMenu(Scanner scan){
        RestTemplate restTemplate = new RestTemplate();
        int check = -1;

        while(check != 0) {
            check = -1;
            System.out.println("=== Facility Management Console ===");
            System.out.println("0: Exit");
            System.out.println("1: Create a new facility");
            System.out.println("2: Delete an existing facility");
            System.out.println("3: View an existing facility");
            System.out.println("4: List existing facilities for a given hotel");
            check = scan.nextInt();
            scan.nextLine();
            switch (check) {
                case 0:
                    return;
                case 1:
                    createFacility(scan, restTemplate);
                    break;
                case 2:
                    deleteFacility(scan, restTemplate);
                    break;
                case 3:
                    viewFacility(scan, restTemplate);
                    break;
                case 4:
                    listFacilities(scan, restTemplate);
                    break;
                default:
                    System.out.println("Invalid entry. Try again.");
            }
        }
        return;
    }
    public static void facilityReservationManagementMenu(Scanner scan){
        RestTemplate restTemplate = new RestTemplate();
        int check = -1;

        while(check != 0) {
            check = -1;
            System.out.println("=== Facility Management Console ===");
            System.out.println("0: Exit");
            System.out.println("1: Create new facility reservation");
            System.out.println("2: Delete an existing facility reservation");
            System.out.println("3: View an existing facility reservation");
            System.out.println("4: List existing facility reservations for a facility");
            check = scan.nextInt();
            scan.nextLine();
            switch (check) {
                case 0:
                    return;
                case 1:
                    createFacilityReservation(scan, restTemplate);
                    break;
                case 2:
                    deleteFacilityReservation(scan, restTemplate);
                    break;
                case 3:
                    viewFacilityReservation(scan, restTemplate);
                    break;
                case 4:
                    listFacilityReservations(scan, restTemplate);
                    break;
                default:
                    System.out.println("Invalid entry. Try again.");
            }
        }
        return;
    }

    /**
     * @param scan Scanner to recieve inputs from the console
     * @param restTemplate Rest template to help with connecting to the database
     */
    public static void createFacility(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Facility ===");
        System.out.println("Enter facility name:");
        String facilityName = scan.nextLine();
        System.out.println("Enter facility description:");
        String description = scan.nextLine();
        System.out.println("Enter hourly facility rent rate:");
        float rate = scan.nextFloat();
        scan.nextLine();
        System.out.println("Enter hotel ID:");
        long hotelId = scan.nextLong();
        scan.nextLine();
        long facilityId = new Random().nextLong();

        String ruri = "facilityName=" + facilityName + "&description=" + description + "&facilityId=" + facilityId + "&rate=" + rate + "&hotelId=" + hotelId;

        String url = "http://localhost:8080/facilities/create?" + ruri;

        restTemplate.put(url, String.class);
    }

    /**
     * Method for creating a new facility reservation
     */
    public static void createFacilityReservation(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Facility Reservation");
        System.out.println("Enter facility ID:");
        long facilityId = scan.nextLong();
        scan.nextLine();
        System.out.println("Enter reservation start date and time:\n(yyyy-MM-dd HH:mm:ss)");
        String startTime = scan.nextLine();
        System.out.println("Enter reservation end date and time:\n(yyyy-MM-dd HH:mm:ss)");
        String endTime = scan.nextLine();
        System.out.println("Enter customer ID:");
        long customerId = scan.nextLong();
        scan.nextLine();
        long reservationId = new Random().nextLong();

        String ruri = "facilityId=" + facilityId + "&startTime=" + startTime + "&endTime=" + endTime + "&customerId=" + customerId + "&reservationId=" + reservationId;

        String url = "http://localhost:8080/facilityReservation/create?" + ruri;

        //System.out.println(url);

        restTemplate.put(url, String.class);
    }

    /**
     * Method for deleting an existing facility
     */
    public static void deleteFacility(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Delete Existing Facility ===");
        System.out.println("Enter facility ID:");
        long facilityId = scan.nextLong();
        scan.nextLine();

        showFacility(facilityId, restTemplate);
        System.out.println("Delete facility shown above?\n0: No\t1: Yes");
        int confirm = scan.nextInt();
        scan.nextLine();
        if(confirm == 1){
            String ruri = "facilityId=" + facilityId;

            restTemplate.put("http://localhost:8080/facilities/delete?" + ruri, String.class);

            return;
        }
        else
            return;

    }

    /**
     * Method for creating an existing facility reservation
     */
    public static void deleteFacilityReservation(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Delete Facility Reservation ===");
        System.out.println("Enter reservation ID:");
        long reservationId = scan.nextLong();
        scan.nextLine();

        showFacilityReservation(reservationId, restTemplate);
        System.out.println("Delete facility shown above?\n0: No\t1: Yes");
        int confirm = scan.nextInt();
        scan.nextLine();
        if(confirm == 1){
            String ruri = "facilityId=" + reservationId;

            restTemplate.put("http://localhost:8080/facilitiyReservation/delete?" + ruri, String.class);

            return;
        }
        else
            return;
    }

    /**
     * Method for viewing an existing facility
     */
    public static void viewFacility(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== View Existing Facility ===");
        System.out.println("Enter facility ID:");
        long facilityId = scan.nextLong();
        scan.nextLine();

        showFacility(facilityId, restTemplate);
        return;
    }

    /**
     * Method for viewing an existing facility reservation
     */
    public static void viewFacilityReservation(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== View Existing Facility Reservation ===");
        System.out.println("Enter reservation ID:");
        long reservationId = scan.nextLong();
        scan.nextLine();

        showFacilityReservation(reservationId, restTemplate);
        return;
    }

    /**
     * A method for showing details of a particular facility
     * @param facilityId ID of the facility whose details are being shown
     */
    public static void showFacility(long facilityId, RestTemplate restTemplate){
        String url = "http://localhost:8080/facilities/get?facilityId=" + facilityId;
        //System.out.println(url);
        String output = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(output);
        return;
    }

    /**
     * A method for showing details of a particular facility reservation
     * @param facilityReservationId ID of the reservation whose details are being shown
     */
    public static void showFacilityReservation(long facilityReservationId, RestTemplate restTemplate){
        String url = "http://localhost:8080/facilityReservation/get?facilityReservationId=" + facilityReservationId;
        //System.out.println(url);
        String output = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(output);
        return;
    }

    /**
     * Method for listing existing facilities in a hotel
     */
    public static void listFacilities(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== List Facilities ===");
        System.out.println("Enter hotel ID:");
        long hotelId = scan.nextLong();
        String url = "http://localhost:8080/facilities/list?hotelId=" + hotelId;
        String output = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(output);
        return;
    }

    /**
     * Method for listing existing facility reservations for a given facility
     */
    public static void listFacilityReservations(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== List Facility Reservations ===");
        System.out.println("Enter facility ID:");
        long facilityId = scan.nextLong();
        String url = "http://localhost:8080/facilityReservation/list?facilityId=" + facilityId;
        String output = restTemplate.getForEntity(url, String.class).getBody();
        System.out.println(output);
        return;
    }
}
