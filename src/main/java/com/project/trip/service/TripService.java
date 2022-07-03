package com.project.trip.service;

import java.util.List;

import com.project.trip.model.Trip;

//Interface TripService
public interface TripService {
	Trip saveTrip(Trip trip);
	List<Trip> getAllTrip();
	Trip getTripById(Long id);
	List<Trip> getAllTripByAgencyId(Long agencyId);
	List<Trip> getAllTripByBusId(Long busId);
	List<Trip> getAllTripBySourceStopId(Long sourceStopId);
	List<Trip> getAllTripByDestStopId(Long destStopId);
	void deleteTrip(Long id);
}
