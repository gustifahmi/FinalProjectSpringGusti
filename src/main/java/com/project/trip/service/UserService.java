package com.project.trip.service;

import java.util.List;

import com.project.trip.model.User;

//Interface UserService
public interface UserService {
	User saveUser(User user);
	List<User> getAllUser();
	User getUserById(Long id);
	User getUserByUsername(String username);
	void deleteUser(Long id);
}
