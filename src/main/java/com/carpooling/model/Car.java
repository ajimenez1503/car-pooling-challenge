package com.carpooling.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Car {
    @Id
    private Long id;

    private int seats;
    private int availableSeats;

    public Car() {
        this.id = Long.valueOf(0);
        this.seats = 0;
        this.availableSeats = 0;
    }

    public Car(Long id, int seat, int availableSeats) {
        this.id = id;
        this.seats = seats;
        this.availableSeats = availableSeats;
    }

    public Car(Long id, int seat) {
        setId(id);
        setSeats(seat);
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Car) {
            Car anotherCar = (Car) obj;
            return anotherCar.getId() == this.id
                    && anotherCar.getSeats() == this.seats
                    && anotherCar.getAvailableSeats() == this.availableSeats;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[id: " + this.id + " seats: " + this.seats + " availableSeats: " + this.availableSeats + "]";
    }

}
