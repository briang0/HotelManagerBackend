package frontend;

import domain.room.Room;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Scanner;

public class RoomManagementConsole {

    public static void mainMenu() {
        int choice = -1;
        Scanner scan = new Scanner(System.in);
        RestTemplate restTemplate = new RestTemplate();
        while (choice != 0) {
            System.out.println("=== Room Management Console");
            System.out.println("0) exit");
            System.out.println("1) New Room");
            System.out.println("2) New Rate");
            System.out.println("3) Register Customer");
            System.out.println("4) Make Reservation");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("=== New Room ===");
                    System.out.println("Room number: ");
                    int roomNumber = scan.nextInt();
                    System.out.println("Room description:");
                    String roomDescription = scan.next();
                    long id = new Random().nextLong();
                    String uri = "roomNumber=" + roomNumber + "&roomId=" + id + "&houseKeepingStatus=" + false + "&roomDescription=" + roomDescription;
                    restTemplate.put("http://localhost:8080/room/create?" + uri,String.class);
                    break;
                case 2:
                    System.out.println("=== New Rate ===");
                    System.out.println("Cost:");
                    float cost = scan.nextFloat();
                    System.out.print("Pay Period:");
                    int payPeriod = scan.nextInt();
                    System.out.println("Currency:");
                    int currency = scan.nextInt();
                    long rateId = new Random().nextLong();
                    String ruri = "rateId=" + rateId + "&cost=" + cost + "&payPeriod=" + payPeriod + "&currency=" + currency;
                    restTemplate.put("http://localhost:8080/rate/create?" + ruri,String.class);
                case 3:
                    System.out.println("=== New Customer ===");
                    System.out.println("DOB (YYYY-MM-dd):");
                    String dob = scan.next();
                    System.out.println("First name:");
                    String firstName = scan.next();
                    System.out.println("Last name:");
                    String lastName = scan.next();
                    long customerId = new Random().nextLong();
                    String curi = "dob=" + dob + "&firstName=" + firstName + "&lastName=" + lastName + "&customerId=" + customerId;
                    restTemplate.put("http://localhost:8080/customer/create?" + curi,String.class);
                    break;
                case 4:
                    System.out.println("== Create reservation ==");
                    System.out.println("Check in time: yyyy-MM-dd HH:mm:ss");
                    String checkIn = scan.nextLine();
                    System.out.println("Checkout time:");
                    String checkout = scan.nextLine();
                    System.out.println("Customer id:");
                    long customerIdReg = scan.nextLong();
                    System.out.println("Rate ID");
                    long rateIdReg = scan.nextLong();
                    long billId = 0;
                    long registrationId = new Random().nextLong();
                    String reguri = "checkInDate=" + checkIn + "&fcheckOutDate=" + checkout + "&reservationId=" + registrationId + "&customerId=" + customerIdReg + "&rateId=" + rateIdReg + "&billId=" + billId;
                    restTemplate.put("http://localhost:8080/reservation/create?" + reguri,String.class);
                    break;
            }
        }
    }

}
