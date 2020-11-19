package domain;

import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * An object that lets the front end interact with the security backend
 * @author Brian Guidarini
 */
public class Security {

    /**
     * Runs a python script that shows the camera feed and uploads the data to the database simultaneously
     * @param scan
     * A scanner to take in user input
     * @param restTemplate
     * A rest template to interact with the web api
     * @throws IOException
     */
    public static void viewVideo(Scanner scan, RestTemplate restTemplate) throws IOException {
        System.out.println("Enter the hotelId for the hotel you want to view the footage for");
        long hotelId = scan.nextLong();
        System.out.println("View a video to analyze");
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String selection = dialog.getDirectory() + dialog.getFile();
        selection = selection.replace("\\", "/");
        System.out.println(selection);
        Process p = Runtime.getRuntime().exec("python MainVision.py " + selection);
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));

        LinkedList<TimeStamp> times = new LinkedList<>();

        String s = null;

        while ((s = stdInput.readLine()) != null) {
            try {
                String[] arr = s.split(" ");
                int numPeople = Integer.parseInt(arr[0]);
                domain.TimeStamp ts = new domain.TimeStamp(Long.parseLong(arr[1]), numPeople);
                times.add(ts);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        for (TimeStamp time : times) {
            String uri = "http://localhost:8080/security/put?hotelId=" + hotelId + "&numPeople=" + time.getNumberOfPeople() + "&timestamp=" + time.getTimestamp();
            restTemplate.put(uri, String.class);
        }
    }

    /**
     * This function retrieves foot traffic analytics from a hotel given a range of time
     * @param scan
     * A scanner to take in user input
     * @param restTemplate
     * A rest template to interact with the web api
     */
    public static void getAnalytics(Scanner scan, RestTemplate restTemplate) {
        System.out.println("Enter the hotel ID for the hotel you want to view analytics for");
        long hotelId = scan.nextLong();
        System.out.println("Enter the timestamp of the start time in the format of YYYY-MM-DDxHH:MM:SS");
        String start = scan.next();
        System.out.println("Enter the timestamp of the end time in the format of YYYY-MM-DDxHH:MM:SS");
        String end = scan.next();
        start = start.replace("x", " ");
        end = end.replace("x", " ");
        String uri = "http://localhost:8080/security/get?hotelId=" + hotelId + "&startTime=" + start + "&endTime=" + end;
        String output = restTemplate.getForObject(uri, String.class);
        System.out.println(output);
    }

}
