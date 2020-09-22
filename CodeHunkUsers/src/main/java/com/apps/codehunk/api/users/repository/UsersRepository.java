package com.apps.codehunk.api.users.repository;

import org.springframework.data.repository.CrudRepository;

import com.apps.codehunk.api.users.data.UserEntity;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByEmail(String emailId);

	UserEntity findByUserId(String userId);
}
