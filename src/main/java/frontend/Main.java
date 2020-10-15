package frontend;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int choice = -1;
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("=== Main Menu ===");
            System.out.println("0) exit");
            System.out.println("1) Hotel Manager");
            System.out.println("2) Reservations");
            choice = scan.nextInt();

            switch (choice) {
                case 0:
                    return;
                case 1:
                    HotelManagementConsole.mainMenu();
                    break;
                case 2:
                    RoomManagementConsole.mainMenu();

            }
        }
    }

}
