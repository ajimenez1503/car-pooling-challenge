package com.carpooling.repository;

import com.carpooling.model.Journey;
import org.springframework.data.repository.CrudRepository;


public interface JourneyRepository extends CrudRepository<Journey, Long> {
}
