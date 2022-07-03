package com.project.trip.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//Table agency
@Entity
@Table(name="agency")
public class Agency {
	
	//Column-column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	
	private String name;
	
	private String details;
	
	//Relasi many-to-one terhadap User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_user_id")
	private User owner;
	
	//Relasi one-to-many terhadap Bus
	@OneToMany(mappedBy = "agency", cascade = CascadeType.ALL)
	private Set<Bus> buses;

	//Relasi one-to-many terhadap Trip
	@OneToMany(mappedBy = "agency", cascade = CascadeType.ALL)
	private Set<Trip> trips;
	
	//Constructor kosong
	public Agency() {
	}
	
	//Constructor dengan parameter
	public Agency(String code, String name, String details, User owner) {
		this.code = code;
		this.name = name;
		this.details = details;
		this.owner = owner;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public void setTrips(Set<Trip> trips) {
		this.trips = trips;
	}
	
	//Getter
	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDetails() {
		return details;
	}
	
	public User getOwner() {
		return owner;
	}

	public Set<Bus> getBuses() {
		return buses;
	}
	
	public Set<Trip> getTrips() {
		return trips;
	}
}
