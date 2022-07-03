package com.project.trip.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.model.Bus;
import com.project.trip.repository.BusRepository;
import com.project.trip.service.BusService;

@Service
@Transactional
public class BusServiceImpl implements BusService {
	
	@Autowired
	private BusRepository busRepository;

	public Bus saveBus(Bus bus) {
		busRepository.save(bus);
		return bus;
	}

	public List<Bus> getAllBus() {
		return busRepository.findAll();
	}

	public Bus getBusById(Long id) {
		return busRepository.findById(id).orElse(new Bus());
	}

	public List<Bus> getAllBusByAgencyId(Long agencyId) {
		return busRepository.findByAgencyId(agencyId);
	}

	public void deleteBus(Long id) {
		busRepository.deleteById(id);
	}
}
