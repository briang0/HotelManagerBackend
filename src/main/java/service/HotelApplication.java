package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point for the web api
 * @Author Brian Guidarini
 */
@SpringBootApplication
public class HotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(service.HotelApplication.class, args);
    }

}
