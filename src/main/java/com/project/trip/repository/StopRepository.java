package com.project.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.trip.model.Stop;

//Interface StopRepository
@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
	List<Stop> findByName (String name);
	List<Stop> findByCode (String code);
}
