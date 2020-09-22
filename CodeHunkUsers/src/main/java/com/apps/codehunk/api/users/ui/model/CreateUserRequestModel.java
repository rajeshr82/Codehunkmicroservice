package com.apps.codehunk.api.users.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {

	@NotNull(message = "First Name cannot be null")
	@Size(min = 3, message = "First name must not be lessthan 3 characters")
	public String firstName;
	@NotNull(message = "Last Name cannot be null")
	@Size(min = 3, message = "Last name must not be lessthan 3 characters")
	public String lastName;

	@NotNull(message = "email cannot be null")
	@Email
	public String email;
	@NotNull(message = "password cannot be null")
	@Size(min = 5, max = 16, message = "password must not be lessthan 5 characters and not max 16 characters")
	public String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
