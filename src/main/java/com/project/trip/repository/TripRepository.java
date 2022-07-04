package com.project.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.trip.model.Stop;
import com.project.trip.model.Trip;

//Interface TripRepository
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
	List<Trip> findAllBySourceStopAndDestStop(Stop sourceStop, Stop destStop);
	
	List<Trip> findByFare(Integer fare);
	List<Trip> findByJourneyTime (String journeyTime);
	List<Trip> findBySourceStop (String sourceStop);
	List<Trip> findByDestStop (String destStop);
	List<Trip> findByBus (String bus);
	List<Trip> findByAgency (String agency);
	
	@Query(value = "SELECT DISTINCT * FROM trip WHERE source_stop_id = :sourceStop"
			+ " AND dest_stop_id = :destStop", nativeQuery = true)
	List<Trip> findTripsByStops(String sourceStop, String destStop);

	@Query(value = "SELECT * FROM trip WHERE agency_id = :agencyId", nativeQuery = true)
	List<Trip> findByAgencyId(Long agencyId);

	@Query(value = "SELECT * FROM trip WHERE bus_id = :busId", nativeQuery = true)
	List<Trip> findByBusId(Long busId);	

	@Query(value = "SELECT * FROM trip WHERE source_stop_id = :sourceStopId", nativeQuery = true)
	List<Trip> findBySourceStopId(Long sourceStopId);
	
	@Query(value = "SELECT * FROM trip WHERE dest_stop_id = :destStopId", nativeQuery = true)
	List<Trip> findByDestStopId(Long destStopId);
}