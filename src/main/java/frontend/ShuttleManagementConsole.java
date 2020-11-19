package frontend;

import domain.ActivityLocation;
import domain.Hotel;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

/**
 * Console for managing hotel shuttles
 *
 */
public class ShuttleManagementConsole {

    public static void shuttleManagementMenu(Scanner scan){
        RestTemplate restTemplate = new RestTemplate();
        int check = -1;
       while(true){
            System.out.println("=== Shuttle Management Menu ===");
            System.out.println("0: Exit");
            System.out.println("1: Create new shuttle");
            System.out.println("2: View all shuttles for a hotel");
            System.out.println("3: Delete an existing shuttle");
            System.out.println("4: Schedule shuttle trip");
            System.out.println("5: View shuttle schedule for a given day");
            System.out.println("6: Delete an existing shuttle trip");

            check = scan.nextInt();
            scan.nextLine();

            switch(check) {
                case 0:
                    return;
                case 1:
                    createShuttle(scan, restTemplate);
                    break;
                case 2:
                    viewShuttles(scan, restTemplate);
                    break;
                case 3:
                    deleteShuttle(scan, restTemplate);
                    break;
                case 4:
                    createShuttleTrip(scan, restTemplate);
                    break;
                case 5:
                    viewSchedule(scan, restTemplate);
                    break;
                case 6:
                    deleteTrip(scan, restTemplate);
                    break;
                default:
                    System.out.println("Invalid entry. Try again.");
                    check = scan.nextInt();
                    scan.nextLine();
            }
        }
    }

    /**
     *
     * @param scan
     * @param restTemplate
     *          These parameters are consistent for the rest of the methods in the class
     */
    public static void createShuttle(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Shuttle ===");
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        System.out.println("Enter shuttle capacity:");
        int capacity = scan.nextInt();
        scan.nextLine();

        String uri = "hotelId=" + hotelId + "&capacity=" + capacity;
        String url = "http://localhost:8080/shuttle/createShuttle?" + uri;

        restTemplate.put(url, String.class);
    }

    public static void viewShuttles(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== View Existing Shuttle for a Hotel ===");
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        String url = "http://localhost:8080/shuttle/viewShuttles?hotelId=" + hotelId;
        String output = restTemplate.getForObject(url, String.class);
        System.out.println(output);
        return;
    }

    public static void deleteShuttle(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Delete Existing Shuttle ===");
        System.out.println("Enter shuttle ID:");
        int shuttleId = scan.nextInt();
        scan.nextLine();
        String url = "http://localhost:8080/shuttle/deleteShuttle?shuttleId=" + shuttleId;
        restTemplate.put(url, String.class);
        return;
    }

    public static void createShuttleTrip(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Shuttle Trip ===");
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        System.out.println("Listing shuttles for hotel:");
        int shuttleId = getShuttleId(scan, restTemplate);
        System.out.println(restTemplate.getForObject("http://localhost:8080/activityLocation/listLocations?hotelId=" + hotelId, String.class));
        System.out.println("Enter location ID:");
        int locationId = scan.nextInt();
        scan.nextLine();
        float distance = ActivityLocation.getLocationDistance(locationId, restTemplate);

        System.out.println("Enter trip date:\n(YYYY-MM-DD)");
        String tripDate = scan.nextLine();
        System.out.println("Enter departure time:\n(hh:mm:ss)");
        String leaveTime = scan.nextLine();
        System.out.println("Enter return time:\n(hh:mm:ss)");
        String returnTime = scan.nextLine();

        String uri = "";
        StringBuilder sb = new StringBuilder(uri);
        sb.append("shuttleId=").append(shuttleId).append("&tripDate=").append(tripDate).append("&leaveTime=").append(leaveTime);
        sb.append("&returnTime=").append(returnTime).append("&locationId=").append(locationId).append("&distance=").append(distance);
        uri=sb.toString();

        String url = "http://localhost:8080/shuttleTrip/createTrip?" + uri;
        //System.out.println(url);

        restTemplate.put(url, String.class);
        return;
    }

    public static void viewSchedule(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== View Shuttle Schedule for Given Day ===");
        int shuttleId = getShuttleId(scan, restTemplate);
        displaySchedule(scan, restTemplate, shuttleId);
        return;
    }

    public static void deleteTrip(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Delete Existing Shuttle Trip ===");
        int shuttleId = getShuttleId(scan, restTemplate);
        displaySchedule(scan, restTemplate, shuttleId);
        System.out.println("Enter trip ID:");
        int tripId = scan.nextInt();
        scan.nextLine();
        String url = "http://localhost:8080/shuttleTrip/deleteTrip?tripId=" + tripId;
        restTemplate.put(url, String.class);
    }

    public static int getShuttleId(Scanner scan, RestTemplate restTemplate){
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        System.out.println("Listing shuttles for hotel:");
        System.out.println(restTemplate.getForObject("http://localhost:8080/shuttle/viewShuttles?hotelId=" + hotelId, String.class));
        System.out.println("Enter shuttle ID:");
        int shuttleId = scan.nextInt();
        scan.nextLine();
        return shuttleId;
    }

    public static void displaySchedule(Scanner scan, RestTemplate restTemplate, int shuttleId){
        System.out.println("Enter date of interest:\n(YYYY-MM-DD)");
        String date = scan.nextLine();

        String uri = "shuttleId=" + shuttleId + "&date=" + date;
        String url = "http://localhost:8080/shuttleTrip/viewSchedule?" + uri;

        String output = restTemplate.getForObject(url, String.class);
        System.out.println(output);
    }
}
