package com.project.trip.payload.request;

import io.swagger.annotations.ApiModelProperty;

//StopRequest, untuk menghandle request body Stop
public class StopRequest {
	
	//Attribute-attribute
	@ApiModelProperty(hidden = true)
	private Long id;
	
	private String code;
	private String name;
	private String detail;
	
	//Constructor kosong
	public StopRequest() {
	}
	
	//Constructor dengan parameter
	public StopRequest(String code, String name, String detail) {
		this.code = code;
		this.name = name;
		this.detail = detail;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setcode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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
	
	public String getDetail() {
		return detail;
	}
	
}