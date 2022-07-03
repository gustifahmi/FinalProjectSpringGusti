package com.project.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.trip.model.Bus;

//Interface BusRepository
public interface BusRepository extends JpaRepository<Bus, Long> {
    @Query(value = "SELECT * FROM bus WHERE agency_id = :agencyId", nativeQuery = true)
    List<Bus> findByAgencyId(Long agencyId);
}