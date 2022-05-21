package com.carpooling.dto;

public class CarDTO {
    private Long id;
    private int seats;

    public  CarDTO() {
        this.id = Long.valueOf(0);
        this.seats = 0;
    }

    public CarDTO(long id, int seats) {
        this.id = id;
        this.seats = seats;
    }

    public Long getId() {
        return this.id;
    }

    public int getSeats() {
        return this.seats;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
