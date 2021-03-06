package com.project.trip.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//Table roles
@Entity
@Table(name = "roles")
public class Role {
	
	//Column-column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private ERole name;
	
	//Constructor kosong
	public Role() {
	}
	
	//Constructor dengan parameter
	public Role(Integer id, ERole name) {
		this.id = id;
		this.name = name;
	}

	//Setter
	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(ERole name) {
		this.name = name;
	}
	
	//Getter
	public Integer getId() {
		return id;
	}

	public ERole getName() {
		return name;
	}
}
