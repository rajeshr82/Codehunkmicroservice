package com.apps.codehunk.api.users.ui.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.codehunk.api.users.service.UserService;
import com.apps.codehunk.api.users.shared.UserDto;
import com.apps.codehunk.api.users.ui.model.CreateUserRequestModel;
import com.apps.codehunk.api.users.ui.model.CreateUserResponseModel;
import com.apps.codehunk.api.users.ui.model.UserResponseModel;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private Environment env;
	
	@Autowired
	private UserService userService;
	
	
	@GetMapping("/status/check")
	public String status() {
		return "WOrking..."+env.getProperty("local.server.port")+", Token --->"+env.getProperty("token.secret");
	}
	
	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity createUser(@Valid @RequestBody CreateUserRequestModel createUserRequestModel) {
		ModelMapper mapper = new ModelMapper();
		UserDto userDto = mapper.map(createUserRequestModel, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		CreateUserResponseModel responseBody = mapper.map(createdUser, CreateUserResponseModel.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}
	
    @GetMapping(value="/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {
       
        UserDto userDto = userService.getUserByUserId(userId); 
        UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);
        
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
