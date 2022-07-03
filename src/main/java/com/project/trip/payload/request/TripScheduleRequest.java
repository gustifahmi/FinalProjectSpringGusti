package com.project.trip.payload.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

//TripScheduleRequest, untuk menghandle request body untuk mendapat data TripSchedule
public class TripScheduleRequest {
	
	//Attribute-attribute
	@ApiModelProperty(hidden = true)
	private Long id;

	@ApiModelProperty(example = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate tripDate;
	
	private Long tripDetailId;

	//Constructor kosong
	public TripScheduleRequest() {
	}

	//Constructor dengan parameter
	public TripScheduleRequest(LocalDate tripDate, Long tripDetailId) {
		this.tripDate = tripDate;
		this.tripDetailId = tripDetailId;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setTripDate(LocalDate tripDate) {
		this.tripDate = tripDate;
	}
	
	public void setTripDetailId(Long tripDetailId) {
		this.tripDetailId = tripDetailId;
	}

	//Getter
	public Long getId() {
		return id;
	}

	public LocalDate getTripDate() {
		return tripDate;
	}
	
	public Long getTripDetailId() {
		return tripDetailId;
	}
}