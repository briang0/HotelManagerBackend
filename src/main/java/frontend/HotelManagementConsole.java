package frontend;

import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Scanner;

public class HotelManagementConsole {

    public static void mainMenu() {
        int choice = -1;
        Scanner scan = new Scanner(System.in);
        RestTemplate restTemplate = new RestTemplate();
        while (choice != 0) {
            System.out.println("=== Room Management Console");
            System.out.println("0) Exit");
            System.out.println("1) New Hotel");
            System.out.println("2) New Room");
            System.out.println("3) New Rate");
            System.out.println("4) View All Rooms");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("=== New Hotel ===");
                    System.out.println("Address: ");
                    scan.nextLine();
                    String address = scan.nextLine();
                    long hotelId = new Random().nextLong();
                    String uri = "address=" + address + "&hotelId=" + hotelId;
                    restTemplate.put("http://localhost:8080/hotel/create?" + uri,String.class);
                    break;
                case 2:
                    System.out.println("=== New Room ===");
                    System.out.println("Room number: ");
                    int roomNumber = scan.nextInt();
                    System.out.println("Room description:");
                    String roomDescription = scan.next();
                    System.out.println("Rate id:");
                    long rateId = scan.nextLong();
                    System.out.println("Hotel id:");
                    long rhotelId = scan.nextLong();
                    long id = new Random().nextLong();
                    String ruri = "roomNumber=" + roomNumber + "&roomId=" + id + "&houseKeepingStatus=" + false + "&roomDescription=" + roomDescription + "&rateId=" + rateId + "&hotelId=" + rhotelId;
                    restTemplate.put("http://localhost:8080/room/create?" + ruri,String.class);
                    break;
                case 3:
                    System.out.println("=== New Rate ===");
                    System.out.println("Cost:");
                    float cost = (float) scan.nextDouble();
                    System.out.println("Pay Period:");
                    int payPeriod = scan.nextInt();
                    System.out.println("Currency:");
                    int currency = scan.nextInt();
                    long rateID = new Random().nextLong();
                    String rateuri = "rateId=" + rateID + "&cost=" + cost + "&payPeriod=" + payPeriod + "&currency=" + currency;
                    restTemplate.put("http://localhost:8080/rate/create?" + rateuri,String.class);
                    break;
                case 4:
                    System.out.println("Hotel ID: ");
                    long vhotelId = scan.nextLong();
                    String vuri = "hotelId=" + vhotelId;
                    String response = restTemplate.getForObject("http://localhost:8080/hotel/get?" + vuri,String.class);
                    System.out.println(response);
            }
        }
    }
}
