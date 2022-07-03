package com.project.trip.payload.request;

//GetTripByStopRequest, untuk menghandle request body untuk mendapat data Trip berdasarkan Stop
public class GetTripByStopRequest {

	//Attribute-attribute
	private Long sourceStopId;
	private Long destStopId;

	//Constructor kosong
	public GetTripByStopRequest() {
	}
	
	//Constructor dengan parameter
	public GetTripByStopRequest(Long sourceStopId, Long destStopId) {
		this.sourceStopId = sourceStopId;
		this.destStopId = destStopId;
	}
	
	//Setter
	public void setSourceStop(Long sourceStopId) {
		this.sourceStopId = sourceStopId;
	}
	
	public void setDestStop(Long destStopId) {
		this.destStopId = destStopId;
	}

	//Getter
	public Long getSourceStop() {
		return sourceStopId;
	}
	
	public Long getDestStop() {
		return destStopId;
	}
}