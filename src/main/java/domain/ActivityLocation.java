package domain;

import com.google.gson.internal.$Gson$Preconditions;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

/**
 * A class containing information about an activity location near a hotel
 * @author Jack Piscitello
 */
public class ActivityLocation {
    private long hotelId;
    private String locationName;
    private String locationAddress;
    private String locationDetails;
    private float distanceFromHotel;
    private int maxPartySize;
    private int locationId;

    public static float getLocationDistance(int locationId, RestTemplate restTemplate){
        String url = "http://localhost:8080/activityLocation/getLocationDistance?locationId=" + locationId;
        String output = restTemplate.getForObject(url, String.class);
        return Float.parseFloat(output);
    }

    public static int getMaxPartySize(int locationId, RestTemplate restTemplate){
        String url = "http://localhost:8080/activityLocation/getMaxPartySize?locationId=" + locationId;
        String output = restTemplate.getForObject(url, String.class);
        return Integer.parseInt(output);
    }
}
