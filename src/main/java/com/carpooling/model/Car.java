package com.carpooling.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Car {
    @Id
    private Long id;

    private int seats;
    private int availableSeats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {

        this.seats = seats;
        this.availableSeats = seats;

    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void updateAvailableSeats(int availableSeats) {

        this.availableSeats = availableSeats;
    }
}
