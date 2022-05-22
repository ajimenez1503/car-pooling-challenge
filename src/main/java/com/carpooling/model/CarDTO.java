package com.carpooling.model;

public class CarDTO {
    private Long id;
    private int seats;

    public CarDTO() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeats() {
        return this.seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CarDTO) {
            CarDTO anotherCar = (CarDTO) obj;
            return anotherCar.getId() == this.id
                    && anotherCar.getSeats() == this.seats;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        result.append("id: " + this.id + " ");
        result.append("seats: " + this.seats + " ");
        result.append("]");

        return result.toString();
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += this.id;
        code += this.seats;

        return code;
    }
}
