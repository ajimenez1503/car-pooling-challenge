package com.carpooling.dto;

public class JourneyDTO {
    private Long id;
    private int people;


    JourneyDTO() {
        this.id = Long.valueOf(0);
        this.people = 0;
    }

    JourneyDTO(Long id, int people) {
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
}
