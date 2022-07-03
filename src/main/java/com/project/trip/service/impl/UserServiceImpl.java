package com.project.trip.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.trip.model.User;
import com.project.trip.repository.UserRepository;
import com.project.trip.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	public User saveUser(User user) {
		userRepository.save(user);
		return user;
	}

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(new User());
	}
	
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
