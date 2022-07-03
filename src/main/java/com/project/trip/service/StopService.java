package com.project.trip.service;

import java.util.List;

import com.project.trip.model.Stop;

//Interface StopService
public interface StopService {
	Stop saveStop(Stop stop);
	List<Stop> getAllStop();
	Stop getStopById(Long id);
	void deleteStop(Long id);
}
