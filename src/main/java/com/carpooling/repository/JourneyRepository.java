package com.carpooling.repository;

import com.carpooling.model.Journey;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JourneyRepository extends JpaRepository<Journey, Long> {
}
