package com.project.trip.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.model.TripSchedule;
import com.project.trip.repository.TripScheduleRepository;
import com.project.trip.service.TripScheduleService;

@Service
@Transactional
public class TripScheduleServiceImpl implements TripScheduleService {
	
	@Autowired
	private TripScheduleRepository tripScheduleRepository;

	public TripSchedule saveTripSchedule(TripSchedule tripSchedule) {
		tripScheduleRepository.save(tripSchedule);
		return tripSchedule;
	}

	public List<TripSchedule> getAllTripSchedule() {
		return tripScheduleRepository.findAll();
	}

	public TripSchedule getTripScheduleById(Long id) {
		return tripScheduleRepository.findById(id).orElse(new TripSchedule());
	}

	public List<TripSchedule> getAllTripScheduleByTripId(Long tripId) {
		return tripScheduleRepository.findByTripId(tripId);
	}
	
	public List<Integer> getAllSeatNumberBooked(Long tripScheduleId) {
		return tripScheduleRepository.findSeatNumberByTripScheduleId(tripScheduleId);
	}
	
	public void deleteTripSchedule(Long id) {
		tripScheduleRepository.deleteById(id);
	}
}
