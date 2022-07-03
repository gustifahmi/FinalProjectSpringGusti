package com.project.trip.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//Table ticket
@Entity
@Table(name = "ticket")
public class Ticket {
	
	//Column-column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int seatNumber;
	
	private Boolean cancellable;
	
	private String journeyDate;
	
	//Relasi many-to-one terhadap TripSchedule
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trip_schedule_id")
	private TripSchedule tripSchedule;
	
	//Relasi many-to-one terhadap User
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "passenger_id")
	private User passenger;

	//Constructor kosong
	public Ticket() {
	}
	
	//Constructor dengan parameter
	public Ticket(int seatNumber, boolean cancellable, String journeyDate,
			TripSchedule tripSchedule, User passenger) {
		this.seatNumber = seatNumber;
		this.cancellable = cancellable;
		this.journeyDate = journeyDate;
		this.tripSchedule = tripSchedule;
		this.passenger = passenger;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public void setCancellable(boolean cancellable) {
		this.cancellable = cancellable;
	}

	public void setJourneyDate(String journeyDate) {
		this.journeyDate = journeyDate;
	}
	
	public void setTripSchedule(TripSchedule tripSchedule) {
		this.tripSchedule = tripSchedule;
	}

	public void setPassenger(User passenger) {
		this.passenger = passenger;
	}
	
	//Getter
	public Long getId() {
		return id;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public boolean getCancellable() {
		return cancellable;
	}
	
	public String getJourneyDate() {
		return journeyDate;
	}
	
	public TripSchedule getTripSchedule() {
		return tripSchedule;
	}

	public User getPassenger() {
		return passenger;
	}
}
