package com.project.trip.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Table user
@Entity
@Table(name = "user_info",
		uniqueConstraints = {
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email")
	})
public class User {

	//Column-column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 50)
	private String username;
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	@NotBlank
	@Size(max = 120)
	@JsonIgnore
	private String password;
	
	@NotBlank
	@Size(max = 120)
	private String firstName;
	
	@NotBlank
	@Size(max = 120)
	private String lastName;
	
	@NotBlank
	@Size(max = 120)
	private String mobileNumber;
	
	//Relasi many-to-many terhadap Role
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	//Relasi one-to-many terhadap Agency
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<Agency> agencies;

	//Relasi one-to-many terhadap Ticket
	@OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
	private Set<Ticket> tickets;
	
	//Constructor kosong
	public User() {
	}
	
	//Constructor dengan parameter
	public User(String firstName, String lastName, String mobileNumber,
			String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
	}
	
	//Setter
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setAgencies(List<Agency> agencies) {
		this.agencies = agencies;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	//Getter
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}

	public List<Agency> getAgencies() {
		return agencies;
	}
	
	public Set<Ticket> getTickets() {
		return tickets;
	}
}
