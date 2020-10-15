package domain;

import domain.room.Room;

import java.util.LinkedList;

public class Hotel {

    private LinkedList<Room> rooms;
    private long hotelId;
    private String address;

    public Hotel(long hotelId, String address) {
        rooms = new LinkedList<>();
        this.hotelId = hotelId;
        this.address = address;
    }

    public LinkedList<Room> getRooms() {
        return rooms;
    }

    public long getHotelId() {
        return hotelId;
    }

    public String getAddress() {
        return address;
    }
}
