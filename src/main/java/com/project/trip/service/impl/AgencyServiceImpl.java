package com.project.trip.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.model.Agency;
import com.project.trip.repository.AgencyRepository;
import com.project.trip.service.AgencyService;

@Service
@Transactional
public class AgencyServiceImpl implements AgencyService {
	
	@Autowired
	private AgencyRepository agencyRepository;

	public Agency saveAgency(Agency agency) {
		agencyRepository.save(agency);
		return agency;
	}

	public List<Agency> getAllAgency() {
		return agencyRepository.findAll();
	}

	public Agency getAgencyById(Long id) {
		return agencyRepository.findById(id).orElse(new Agency());
	}

	public List<Agency> getAllAgencyByOwnerId(Long ownerId) {
		return agencyRepository.findByOwnerUserId(ownerId);
	}
	
	public void deleteAgency(Long id) {
		agencyRepository.deleteById(id);
	}
}
