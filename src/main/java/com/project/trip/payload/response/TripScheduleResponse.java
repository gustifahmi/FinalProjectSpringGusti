package com.project.trip.payload.response;

//TripScheduleResponse untuk menampilkan response dari objek TripSchedule ke dalam bentuk json
public class TripScheduleResponse{
	
	//Attribute-attribute
	private Long id;
	private String tripDate;
	private int availableSeats; 
	private Long tripDetailId;

	//Constructor kosong
	public TripScheduleResponse() {
	}

	//Constructor dengan parameter
	public TripScheduleResponse(Long id, String tripDate, int availableSeats, Long tripDetailId) {
		this.id = id;
		this.tripDate = tripDate;
		this.availableSeats = availableSeats;
		this.tripDetailId = tripDetailId;
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
	
	public void setTripDetailid(Long tripDetailId) {
		this.tripDetailId = tripDetailId;
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
	
	public Long getTripDetailId() {
		return tripDetailId;
	}
}