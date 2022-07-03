package com.project.trip.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//SignupRequest, untuk menghandle request body untuk sign up
public class SignupRequest {
	
	//Attribute-attribute
	@NotBlank
	@Size(min = 3, max = 50)
	private String username;
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	@NotBlank
	@Size(max = 120)
	private String firstName;
	
	@NotBlank
	@Size(max = 120)
	private String lastName;
	
	@NotBlank
	@Size(max = 120)
	private String mobileNumber;
	
	private Set<String> role;
	
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;
    
	//Constructor kosong
	public SignupRequest() {
	}

	//Constructor dengan parameter
	public SignupRequest(String username, String email, String firstName, String lastName,
			String mobileNumber, Set<String> role, String password) {
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.role = role;
		this.password = password;
	}
	
	//Setter
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setEmail(String email) {
		this.email = email;
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

	public void setRole(Set<String> role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//Getter
    public String getUsername() {
    	return username;
    }
    
    public String getEmail() {
    	return email;
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
    
    public Set<String> getRole() {
    	return role;
    }

	public String getPassword() {
		return password;
	}
}