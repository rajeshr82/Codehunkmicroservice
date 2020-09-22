package com.apps.codehunk.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import feign.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class PhotoAppApiUsersApplication {

	@Autowired
	Environment environment;
	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUsersApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
	
	@Bean
	@Profile("production")
	Logger.Level getFeignLogger(){
		return Logger.Level.BASIC;
	}

	@Bean
	@Profile("!production")
	Logger.Level getFeignDefaultLogger(){
		return Logger.Level.FULL;
	}

	@Bean
	@Profile("production")
	public String createProductionBean() {
		System.out.println("Production Bean created myapplication.environment == "+ environment.getProperty("myapplication.environment"));
		return "Production Bean";
	}

	@Bean
	@Profile("!production")
	public String createNorProductionBean() {
		System.out.println("Non Production Bean created  myapplication.environment =="+ environment.getProperty("myapplication.environment"));
		return "Not Production Bean";
	}

	@Bean
	@Profile("default")
	public String createDevelopmentBean() {
		System.out.println("Development bean created myapplication.environment == "+ environment.getProperty("myapplication.environment"));
		return "Development Bean";
	}

	/*@Bean
	FeignErrorDecoder getFeignErrorDecoder() {
		return new FeignErrorDecoder();
	}*/
}
