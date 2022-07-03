package com.project.trip.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//Table stop
@Entity
@Table(name = "stop")
public class Stop {
	
	//Column-column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	
	private String name;
	
	private String detail;

	//Relasi one-to-many terhadap Trip (untuk sourceStop)
	@OneToMany(mappedBy = "sourceStop", cascade = CascadeType.ALL)
	private Set<Trip> sourceTrips;

	//Relasi one-to-many terhadap Trip (untuk destStop)
	@OneToMany(mappedBy = "destStop", cascade = CascadeType.ALL)
	private Set<Trip> destTrips;
	
	//Constructor kosong
	public Stop() {
	}

	//Constructor dengan parameter
	public Stop(String code, String name, String detail) {
		this.code = code;
		this.name = name;
		this.detail = detail;
	}

	//Setter
	public void setId(Long id) {
		this.id = id;
	}

	public void setCode(String code) {
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
