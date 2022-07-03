package com.project.trip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.trip.model.Agency;
import com.project.trip.model.User;

//Interface AgencyRepository
@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

	Agency findByOwner(User owner);

	@Query(value = "SELECT * FROM agency WHERE owner_user_id = :ownerId", nativeQuery = true)
	List<Agency> findByOwnerUserId(Long ownerId);
}