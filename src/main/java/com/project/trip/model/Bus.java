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

//Table bus
@Entity
@Table(name="bus")
public class Bus {
	
	//Column-column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	
	private int capacity;
	
	private String make;
	
	//Relasi many-to-one terhadap Agency
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agency_id")
	private Agency agency;

	//Relasi one-to-many terhadap Trip
	@OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
	private Set<Trip> trips;
	
	//Constructor kosong
	public Bus() {
	}
	
	//Constructor dengan parameter
	public Bus(String code, int capacity, String make, Agency agency) {
		this.code = code;
		this.capacity = capacity;
		this.make = make;
		this.agency = agency;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
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

	public int getCapacity() {
		return capacity;
	}
	
	public String getMake() {
		return make;
	}
	
	public Agency getAgency() {
		return agency;
	}

	public Set<Trip> getTrips() {
		return trips;
	}
}
