package com.project.trip.payload.request;

import io.swagger.annotations.ApiModelProperty;

//TicketRequest, untuk menghandle request body Ticket
public class ReservationRequest {
	
	//Attribute-attribute
	@ApiModelProperty(hidden = true)
	private Long id;
	
	private int seatNumber;
	private Boolean cancellable;
	private Long tripScheduleId;

	//Constructor kosong
	public ReservationRequest() {
	}

	//Constructor dengan parameter
	public ReservationRequest(int seatNumber, Boolean cancellable, Long tripScheduleId) {
		this.seatNumber = seatNumber;
		this.cancellable = cancellable;
		this.tripScheduleId = tripScheduleId;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}
	
	public void setCancellable(Boolean cancellable) {
		this.cancellable = cancellable;
	}
	
	public void setTripScheduleId(Long tripScheduleId) {
		this.tripScheduleId = tripScheduleId;
	}

	//Getter
	public Long getId() {
		return id;
	}
	
	public int getSeatNumber() {
		return seatNumber;
	}
	
	public Boolean getCancellable() {
		return cancellable;
	}

	public Long getTripScheduleId() {
		return tripScheduleId;
	}
}