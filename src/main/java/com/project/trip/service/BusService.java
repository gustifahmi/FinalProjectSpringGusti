package com.project.trip.service;

import java.util.List;

import com.project.trip.model.Bus;

//Interface BusService
public interface BusService {
	Bus saveBus(Bus bus);
	List<Bus> getAllBus();
	List<Bus> getAllBusByAgencyId(Long agencyId);
	Bus getBusById(Long id);
	void deleteBus(Long id);
}
