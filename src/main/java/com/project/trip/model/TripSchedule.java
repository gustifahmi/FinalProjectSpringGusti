package com.project.trip.model;
	
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//Table trip_schedule
@Entity
@Table(name = "trip_schedule")
public class TripSchedule {
	
	//Column-column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tripDate;
	
	private int availableSeats;

	//Relasi one-to-one terhadap Trip
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trip_id")
	private Trip tripDetail;

	//Relasi one-to-many terhadap Ticket
	@OneToMany(mappedBy = "tripSchedule", cascade = CascadeType.ALL)
	private Set<Ticket> ticketsSold;
	
	//Constructor kosong
	public TripSchedule() {
	}
	
	//Constructor dengan parameter
	public TripSchedule(String tripDate, int availableSeats, Trip tripDetail) {
		this.tripDate = tripDate;
		this.availableSeats = availableSeats;
		this.tripDetail = tripDetail;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}
	
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public void setTripDetail(Trip tripDetail) {
		this.tripDetail = tripDetail;
	}

	public void setTripDetail(Set<Ticket> ticketsSold) {
		this.ticketsSold = ticketsSold;
	}

	//Getter
	public Long getId() {
		return id;
	}

	public String getTripDate() {
		return tripDate;
	}
	
	public int getAvailableSeats() {
		return availableSeats;
	}
	
	public Trip getTripDetail() {
		return tripDetail;
	}
	
	public Set<Ticket> getTicketsSold() {
		return ticketsSold;
	}
}
