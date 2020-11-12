package domain;

import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Scanner;

public class Security {

    public static void viewVideo(Scanner scan, RestTemplate restTemplate) throws IOException {
        System.out.println("Enter the hotelId for the hotel you want to view the footage for");
        long hotelId = scan.nextLong();
        System.out.println("View a video to analyze");
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String selection = dialog.getDirectory() + dialog.getFile();
        Process p = Runtime.getRuntime().exec("python MainVision.py " + selection);
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));

        LinkedList<TimeStamp> times = new LinkedList<>();

        String s = null;

        while ((s = stdInput.readLine()) != null) {
            int numPeople = Integer.parseInt(s);
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            domain.TimeStamp ts = new domain.TimeStamp(currentTime, numPeople);
            times.add(ts);
        }

        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        for (TimeStamp time : times) {
            String uri = "http://localhost:8080/security/put?hotelId=" + hotelId + "&numPeople=" + time.getNumberOfPeople() + "&timestamp=" + time.getTimestamp();
            restTemplate.put(uri, String.class);
        }
    }

    public static void getAnalytics(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter the hotel ID for the hotel you want to view analytics for");
        long hotelId = scan.nextLong();
        System.out.println("Enter the timestamp of the start time in the format of YYYY-MM-DDxHH:MM:SS");
        String start = scan.nextLine();
        System.out.println("Enter the timestamp of the end time in the format of YYYY-MM-DDxHH:MM:SS");
        String end = scan.nextLine();
        String uri = "http://localhost:8080/security/get?hotelId=" + hotelId + "&startTime=" + start + "&endTime=" + end;
        String output = restTemplate.getForObject(uri, String.class);
        System.out.println(output);
    }

}
