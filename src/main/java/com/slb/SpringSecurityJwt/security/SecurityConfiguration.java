package com.slb.SpringSecurityJwt.security;
import com.slb.SpringSecurityJwt.filter.JwtAuthFilter;
import com.slb.SpringSecurityJwt.service.MyUserDetailsService;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration{

//	@Autowired
//	MyUserDetailsService MyUserDetailsService;
	
	 @Autowired
	 private JwtAuthFilter authFilter;
	
	 @Bean
	 public UserDetailsService userDetailsService() {
		 return new MyUserDetailsService();
	 }

	  @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
		  http.csrf().disable()
		  	  .authorizeHttpRequests().requestMatchers("/","/authenticate").permitAll()
		  	  .and()
		  	  .authorizeHttpRequests()
		  	  .requestMatchers("/user","/admin")
		  	  .authenticated().and()
		  	  .exceptionHandling().and().sessionManagement()
		  	  .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		  http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
		  http.authenticationProvider(autenticationProvider()); 
		  return http.build();
		 
	  }
	  
	  @Bean 
	  public AuthenticationProvider autenticationProvider() {
	  DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
	  provider.setUserDetailsService(userDetailsService());
	 provider.setPasswordEncoder( getPasswordEncoder() );
	 return provider;
	  
	  }
	  @Bean
	    public PasswordEncoder getPasswordEncoder() {
	        return NoOpPasswordEncoder.getInstance();
	    }
	
	 @Bean
	 public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	     return authenticationConfiguration.getAuthenticationManager();
	 }
	 

}
