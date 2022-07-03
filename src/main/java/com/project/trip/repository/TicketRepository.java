package com.project.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.trip.model.Ticket;

//Interface TicketRepository
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByPassengerId(Long passengerId);

	@Query(value = "SELECT * FROM ticket WHERE trip_schedule_id = :tripScheduleId", nativeQuery = true)
	List<Ticket> findByTripScheduleId(Long tripScheduleId);
	
	@Query(value = "SELECT COUNT(*) FROM trip_schedule ts JOIN ticket t ON ts.id = t.trip_schedule_id "
			+ "JOIN user_info ui ON t.passenger_id = ui.id WHERE ui.id = :passengerId "
			+ "AND ts.id = :tripScheduleId", nativeQuery = true)
	int numberOfBookedSeatsByPassengerId(Long passengerId, Long tripScheduleId);
}
