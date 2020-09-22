package com.apps.codehunk.api.users.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.apps.codehunk.api.users.data.AlbumsServiceClient;
import com.apps.codehunk.api.users.data.UserEntity;
import com.apps.codehunk.api.users.repository.UsersRepository;
import com.apps.codehunk.api.users.service.UserService;
import com.apps.codehunk.api.users.shared.UserDto;
import com.apps.codehunk.api.users.ui.model.AlbumResponseModel;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	Environment environment;
	
	@Autowired
	AlbumsServiceClient albumsServiceClient;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		usersRepository.save(userEntity);
		return mapper.map(userEntity, UserDto.class);
	}
	@Override
	public UserDto retrieveUserDetailsByEmail(String emailId) {
		UserEntity userentity = usersRepository.findByEmail(emailId);
		if (userentity == null) {
			throw new UsernameNotFoundException("user email not found");
		}
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = mapper.map(userentity, UserDto.class);

		return userDto;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userentity = usersRepository.findByEmail(username);
		if (userentity == null) {
			throw new UsernameNotFoundException("user name not found");
		}
		return new User(userentity.getEmail(), userentity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}
	@Override
	public UserDto getUserByUserId(String userId) {
		 UserEntity userEntity = usersRepository.findByUserId(userId);     
	        if(userEntity == null) throw new UsernameNotFoundException("User not found");
	        
	       UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
	        // Following is rest template calling HTTP
	        //String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
	        
	      //  ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {});
	      //  List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
	       logger.info("Before calling Albums microservice");
	       List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userId);
	       logger.info("After calling Albums microservice");

	        userDto.setAlbums(albumsList);
	        return userDto;
	}

}
