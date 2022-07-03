package com.project.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.trip.model.TripSchedule;

//Interface TripScheduleRepository
@Repository
public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {
	List<TripSchedule> findAllByTripDate(String tripDate);
	
	List<TripSchedule> findByTripDate(String tripDate);
	
	@Query(value = "SELECT DISTINCT * FROM trip_schedule WHERE trip_date = :tripDate", nativeQuery = true)
    List<TripSchedule> findTripScheduleByDate(String tripDate);

	@Query(value = "SELECT * FROM trip_schedule WHERE trip_id = :tripId", nativeQuery = true)
	List<TripSchedule> findByTripId(Long tripId);
	
	@Query(value = "SELECT t.seat_number FROM trip_schedule ts JOIN ticket t ON ts.id = t.trip_schedule_id"
			+ " WHERE t.trip_schedule_id = :tripScheduleId", nativeQuery = true)
	List<Integer> findSeatNumberByTripScheduleId(Long tripScheduleId);
}