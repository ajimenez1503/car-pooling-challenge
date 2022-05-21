package com.carpooling.model;

import com.carpooling.dto.CarDTO;

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

    public Car(Long id, int seat) {
        setId(id);
        setSeats(seat);
    }

    public Car(CarDTO car) {
        setId(car.getId());
        setSeats(car.getSeats());
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
        StringBuilder result = new StringBuilder();
        result.append("[");
        result.append("id: " + this.id + " ");
        result.append("seats: " + this.seats + " ");
        result.append("availableSeats: " + this.availableSeats + " ");
        result.append("]");

        return result.toString();
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += this.id;
        code += this.seats;
        code += this.availableSeats;

        return code;
    }
}
