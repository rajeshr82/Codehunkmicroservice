package com.apps.codehunk.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.apps.codehunk.api.users.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	Environment environment;
	
	@Autowired
	UserService userService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println(environment.getProperty("gateway.ip"));
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/users/**").permitAll()
		.and()
		.addFilter(getAuthenticationFilter());

		http.headers().frameOptions().disable();
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService,environment,authenticationManager());
		//authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
		return authenticationFilter;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder authenticationBuilder) throws Exception{
		authenticationBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}

}
