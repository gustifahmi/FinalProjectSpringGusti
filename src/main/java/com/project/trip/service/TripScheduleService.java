package com.project.trip.service;

import java.util.List;

import com.project.trip.model.TripSchedule;

//Interface TripScheduleService
public interface TripScheduleService {
	TripSchedule saveTripSchedule(TripSchedule tripSchedule);
	List<TripSchedule> getAllTripSchedule();
	TripSchedule getTripScheduleById(Long id);
	List<TripSchedule> getAllTripScheduleByTripId(Long tripId);
	List<Integer> getAllSeatNumberBooked(Long tripScheduleId);
	void deleteTripSchedule(Long id);
}
