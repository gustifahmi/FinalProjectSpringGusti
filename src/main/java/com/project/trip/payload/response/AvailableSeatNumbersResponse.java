package com.project.trip.payload.response;

import java.util.List;

//BusResponse untuk menampilkan set number yang tersedia dari suatu trip schedule ke dalam bentuk json
public class AvailableSeatNumbersResponse {
	private Long tripScheduleId;
	private List<Integer> availableSeatNumbers;
	
	//Constructor kosong
	public AvailableSeatNumbersResponse() {
	}
	
	//Constructor dengan parameter
	public AvailableSeatNumbersResponse(Long tripScheduleId, List<Integer> availableSeatNumbers) {
		this.tripScheduleId = tripScheduleId;
		this.availableSeatNumbers = availableSeatNumbers;
	}
	
	//Setter
	public void setTripScheduleId(Long tripScheduleId) {
		this.tripScheduleId = tripScheduleId;
	}
	
	public void setAvailableSeatNumbers(List<Integer> availableSeatNumbers) {
		this.availableSeatNumbers = availableSeatNumbers;
	}
	
	//Getter
	public Long getTripScheduleId() {
		return tripScheduleId;
	}
	
	public List<Integer> getAvailableSeatNumbers() {
		return availableSeatNumbers;
	}
}
