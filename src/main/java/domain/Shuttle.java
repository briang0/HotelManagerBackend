package domain;

import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class Shuttle {
    private long hotelId;
    private long shuttleId;
    private int passengerCapacity;

    public static int getShuttleCapacity(int shuttleId, RestTemplate restTemplate){
        String url = "http://localhost:8080/shuttle/getShuttleCapacity?shuttleId=" + shuttleId;
        int capacity = Integer.parseInt(restTemplate.getForObject(url, String.class));
        return capacity;
    }
}
