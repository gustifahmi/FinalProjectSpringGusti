package com.project.trip.service;

import java.util.List;

import com.project.trip.model.Agency;

//Interface AgencyService
public interface AgencyService {
	Agency saveAgency(Agency agency);
	List<Agency> getAllAgency();
	Agency getAgencyById(Long id);
	List<Agency> getAllAgencyByOwnerId(Long ownerId);
	void deleteAgency(Long id);
}
