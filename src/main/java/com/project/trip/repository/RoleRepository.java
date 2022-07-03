package com.project.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.trip.model.ERole;
import com.project.trip.model.Role;

//Interface RoleRepository
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(ERole name);
	Role findById(int id);
}