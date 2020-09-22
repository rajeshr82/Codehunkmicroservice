package com.apps.codehunk.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.apps.codehunk.api.users.shared.UserDto;

public interface UserService extends UserDetailsService{
	UserDto createUser(UserDto userDto);

	UserDto retrieveUserDetailsByEmail(String userName);

	UserDto getUserByUserId(String userId);
}
