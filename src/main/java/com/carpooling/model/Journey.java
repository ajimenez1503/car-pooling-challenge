package com.carpooling.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Journey {
    @Id
    private Long id;

    private int people;

    @OneToOne
    private Car car;

    public Journey() {
        this.id = Long.valueOf(0);
        this.people = 0;
        this.car = null;
    }

    public Journey(Long id, int people) {
        this.id = id;
        this.people = people;
        this.car = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        result.append("id: " + this.id);
        result.append("people: " + this.people);
        if (this.car != null) {
            result.append("car: " + this.car.toString());
        }
        result.append("]");

        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Journey) {
            Journey anotherJourney = (Journey) obj;
            return anotherJourney.getId() == this.id
                    && anotherJourney.getPeople() == this.people
                    && anotherJourney.getCar() == this.car;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += this.id;
        code += this.people;
        if (this.car != null) {
            code += this.car.hashCode();
        }
        return code;
    }
}
