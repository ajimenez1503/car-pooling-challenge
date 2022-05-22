package com.carpooling.model;

import com.carpooling.model.Journey;

public class JourneyDTO {
    private Long id;
    private int people;


    JourneyDTO() {
        this.id = Long.valueOf(0);
        this.people = 0;
    }

    public JourneyDTO(Long id, int people) {
        this.id = id;
        this.people = people;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        result.append("id: " + this.id + " ");
        result.append("people: " + this.people + " ");
        result.append("]");

        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof JourneyDTO) {
            JourneyDTO anotherJourney = (JourneyDTO) obj;
            return anotherJourney.getId() == this.id
                    && anotherJourney.getPeople() == this.people;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = 0;
        code += this.id;
        code += this.people;
        return code;
    }
}
