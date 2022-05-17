package com.carpooling.repository;

import com.carpooling.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface JourneyRepository extends JpaRepository<Journey, Long> {
}
