package frontend;

import domain.ActivityLocation;
import domain.Hotel;
import domain.Shuttle;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class ActivityPlanningConsole {

    /**
     *
     * @param scan
     */
    public static void activityPlanningMenu(Scanner scan){
        RestTemplate restTemplate = new RestTemplate();


        int check = -1;
        while(true) {
            System.out.println("=== Activity Planning Menu ===");
            System.out.println("0: Exit");
            System.out.println("1: Add new activity location");
            System.out.println("2: List existing activity locations for a hotel");
            System.out.println("3: Delete existing activity location");
            System.out.println("4: Create new activity event");
            System.out.println("5: Register for event");
            System.out.println("6: Cancel event");
            System.out.println("7: Complete event and charge customers");
            check = scan.nextInt();
            scan.nextLine();

            switch (check) {
                case 0:
                    return;
                case 1:
                    addLocation(scan, restTemplate);
                    break;
                case 2:
                    listLocations(scan, restTemplate);
                    break;
                case 3:
                    deleteLocation(scan, restTemplate);
                    break;
                case 4:
                    createEvent(scan, restTemplate);
                    break;
                case 5:
                    registerForEvent(scan, restTemplate);
                    break;
                case 6:
                    cancelEvent(scan, restTemplate);
                    break;
                case 7:
                    completeEvent(scan, restTemplate);
                    break;
                default:
                    System.out.println("Invalid entry. Try again.");

            }
        }
    }

    public static void addLocation(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Add New Event Location ===");
        System.out.println("Enter the location name:");
        String name = scan.nextLine();
        System.out.println("Enter the location address:");
        String address = scan.nextLine();
        System.out.println("Enter party size limit for the location:");
        int partySize = scan.nextInt();
        scan.nextLine();
        System.out.println("Enter location description/details:");
        String details = scan.nextLine();
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        System.out.println("Enter distance from the hotel in miles:");
        float distance = scan.nextFloat();


        String uri = "name=" + name + "&address=" + address + "&partySize=" + partySize + "&details=" + details + "&distance=" + distance + "&hotelId=" + hotelId;
        String url = "http://localhost:8080/activityLocation/addLocation?" + uri;
        //System.out.println(url);
        restTemplate.put(url, String.class);
        return;
    }

    public static void listLocations(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== List Event Locations for a Hotel ===");
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        String url = "http://localhost:8080/activityLocation/listLocations?hotelId=" + hotelId;
        //System.out.println(url);
        String output = restTemplate.getForObject(url, String.class);
        System.out.println(output);
        return;
    }


    public static void deleteLocation(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Delete Existing Event Location ===");
        System.out.println("Enter location ID:");
        int locationId = scan.nextInt();
        scan.nextLine();
        String url = "http://localhost:8080/activityLocation/deleteLocation?locationId=" + locationId;
        System.out.println(url);
        restTemplate.put(url, String.class);
        return;
    }

    public static void createEvent(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Create New Event ===");
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        System.out.println("Select one of the following locations:");
        System.out.println(restTemplate.getForObject("http://localhost:8080/activityLocation/listLocations?hotelId="+hotelId, String.class));
        System.out.println("Enter location ID:");
        int locationId = scan.nextInt();
        System.out.println(restTemplate.getForObject("http://localhost:8080/shuttle/viewShuttles?hotelId=" + hotelId, String.class));
        System.out.println("Select shuttle ID:");
        int shuttleId = scan.nextInt();
        scan.nextLine();
        int shuttleCapacity = Shuttle.getShuttleCapacity(shuttleId, restTemplate);
        int maxPartySize = ActivityLocation.getMaxPartySize(locationId, restTemplate);
        int capacity = 0;
        if(shuttleCapacity > maxPartySize){
            capacity = maxPartySize;
        }
        else{
            capacity = shuttleCapacity;
        }

        System.out.println("Enter event date:\n(YYYY-MM-DD)");
        String eventDate = scan.nextLine();
        System.out.println("Enter event start time:\n(hh:mm:ss)");
        String startTime = scan.nextLine();
        System.out.println("Enter event end time:\n(hh:mm:ss)");
        String endTime = scan.nextLine();

        System.out.println("Enter cost per person in USD:");
        float cost = scan.nextFloat();
        scan.nextLine();

        String uri = "";
        StringBuilder sb = new StringBuilder(uri);
        sb.append("hotelId=").append(hotelId).append("&locationId=").append(locationId).append("&eventDate=").append(eventDate);
        sb.append("&startTime=").append(startTime).append("&endTime=").append(endTime).append("&capacity=").append(capacity);
        sb.append("&cost=").append(cost).append("&shuttleId=").append(shuttleId);
        uri = sb.toString();

        String url = "http://localhost:8080/activityEvent/createEvent?" + uri;

        restTemplate.put(url, String.class);

        float distance = ActivityLocation.getLocationDistance(locationId, restTemplate);
        String shuttleUri = "";
        sb = new StringBuilder(shuttleUri);
        sb.append("shuttleId=").append(shuttleId).append("&tripDate=").append(eventDate).append("&leaveTime=").append(startTime);
        sb.append("&returnTime=").append(endTime).append("&locationId=").append(locationId).append("&distance=").append(distance);
        shuttleUri=sb.toString();
        String shuttleUrl = "http://localhost:8080/shuttleTrip/createTrip?" + shuttleUri;
        restTemplate.put(shuttleUrl, String.class);
        return;
    }

    public static int getEventId(Scanner scan, RestTemplate restTemplate){
        long hotelId = Hotel.selectHotel(scan, restTemplate);
        String url = "http://localhost:8080/activityEvent/listEvents?hotelId=" + hotelId;
        String toPrint = restTemplate.getForObject(url, String.class);
        System.out.print(toPrint);
        System.out.println("Enter event number:");
        int eventId = scan.nextInt();
        scan.nextLine();
        return eventId;
    }

    public static void registerForEvent(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Register for Event ===");
        int eventId = getEventId(scan, restTemplate);
        float cost = getCost(eventId, restTemplate);
        System.out.println("Enter customer ID:");
        long customerId = scan.nextLong();
        System.out.println("How many participants in the group:");
        int participants = scan.nextInt();
        scan.nextLine();

        String uri = "eventId=" + eventId + "&cost=" + cost + "&customerId=" + customerId + "&participants=" + participants;
        String url = "http://localhost:8080/activityEvent/registerForEvent?" + uri;
        restTemplate.put(url, String.class);
        System.out.println(url);
        return;
    }

    public static float getCost(int eventId, RestTemplate restTemplate){
        String url = "http://localhost:8080/activityEvent/getEventCost?eventId=" + eventId;
        String output = restTemplate.getForObject(url, String.class);
        float cost = Float.parseFloat(output);
        System.out.println(url);
        return cost;
    }

    public static void cancelEvent(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Cancel Event ===");
        int eventId = getEventId(scan, restTemplate);
        String url = "http://localhost:8080/activityEvent/cancelEvent?eventId=" + eventId;
        restTemplate.put(url, String.class);
        return;
    }


    public static void chargeCustomers(int eventId, RestTemplate restTemplate){
        String url = "http://localhost:8080/activityEvent/chargeCustomers?eventId=" + eventId;
        restTemplate.put(url, String.class);
    }

    public static void completeEvent(Scanner scan, RestTemplate restTemplate){
        System.out.println("=== Complete Event and Charge Customers ===");
        int eventId = getEventId(scan, restTemplate);
        chargeCustomers(eventId, restTemplate);
        return;
    }

}
