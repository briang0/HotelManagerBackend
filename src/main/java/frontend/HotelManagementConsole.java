package frontend;

import domain.Hotel;
import domain.room.Rate;
import domain.room.Room;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

/**
 * A class to provide a user interface for the hotel management
 * @Author: Brian Guidarini
 */
public class HotelManagementConsole {

    /**
     * The main menu loop for the class
     * @param scan A Scanner to take in user input
     */
    public static void mainMenu(Scanner scan) {
        int choice = -1;
        //Scanner scan = new Scanner(System.in);
        RestTemplate restTemplate = new RestTemplate();
        while (choice != 0) {
            System.out.println("=== Room Management Console");
            System.out.println("0) Exit");
            System.out.println("1) New Hotel");
            System.out.println("2) New Room");
            System.out.println("3) New Rate");
            System.out.println("4) View All Rooms");
            System.out.println("5) Register multiple rooms");
            System.out.println("6) View all hotels");
            System.out.println("7) View all rates");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    Hotel.registerHotel(scan, restTemplate);
                    break;
                case 2:
                    Room.registerRoom(scan, restTemplate);
                    break;
                case 3:
                    Rate.registerRate(scan, restTemplate);
                    break;
                case 4:
                    Hotel.viewRooms(scan, restTemplate);
                    break;
                case 5:
                    Room.registerMultipleRooms(scan, restTemplate);
                    break;
                case 6:
                    Hotel.getAllHotels(scan, restTemplate);
                    break;
                case 7:
                    Rate.getAllRatesAssociatedToHotel(scan, restTemplate);
                    break;

            }
        }
    }

}
