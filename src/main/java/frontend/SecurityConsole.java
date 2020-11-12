package frontend;

import domain.Listing;
import domain.Security;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Scanner;

public class SecurityConsole {

    public static void mainMenu(Scanner scan) throws IOException {
        int choice = -1;
        RestTemplate restTemplate = new RestTemplate();
        while (choice != 0) {
            System.out.println("=== Room Management Console");
            System.out.println("0) exit");
            System.out.println("1) View Footage");
            System.out.println("2) View Analytics");
            choice = scan.nextInt();
            switch (choice) {
                case 1:
                    Security.viewVideo(scan, restTemplate);
                    break;
                case 2:
                    Security.getAnalytics(scan, restTemplate);
            }
        }
    }

}
