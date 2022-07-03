package com.project.trip.payload.request;

import io.swagger.annotations.ApiModelProperty;

//TripRequest, untuk menghandle request body Trip
public class TripRequest {
	
	//Attribute-attribute
	@ApiModelProperty(hidden = true)
	private Long id;
	
	private int fare;
	private int journeyTime;
	private Long sourceStopId;
	private Long destStopId;
	private Long busId;
	
	//Constructor kosong
	public TripRequest() {
	}

	//Constructor dengan parameter
	public TripRequest(int fare, int journeyTime, Long sourceStopId, Long destStopId, Long busId) {
		this.fare = fare;
		this.journeyTime = journeyTime;
		this.sourceStopId = sourceStopId;
		this.destStopId = destStopId;
		this.busId = busId;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setFare(int fare) {
		this.fare = fare;
	}

	public void setJourneyTime(int journeyTime) {
		this.journeyTime = journeyTime;
	}

	public void setSourceStopId(Long sourceStopId) {
		this.sourceStopId = sourceStopId;
	}

	public void setDestStopId(Long destStopId) {
		this.destStopId = destStopId;
	}
	
	public void setBusId(Long busId) {
		this.busId = busId;
	}
	
	//Getter
	public Long getId() {
		return id;
	}

	public int getFare() {
		return fare;
	}

	public int getJourneyTime() {
		return journeyTime;
	}

	public Long getSourceStopId() {
		return sourceStopId;
	}

	public Long getDestStopId() {
		return destStopId;
	}

	public Long getBusId() {
		return busId;
	}
}