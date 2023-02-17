package com.slb.SpringSecurityJwt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.slb.SpringSecurityJwt.model.AuthenticationRequest;
import com.slb.SpringSecurityJwt.service.JwtService;
import com.slb.SpringSecurityJwt.service.MyUserDetailsService;

import jakarta.annotation.security.RolesAllowed;

@RestController
public class UserController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@RequestMapping("/")
	public String index() {
		return "<h1>Welcome</h1>";
	}
	
	@RequestMapping("/user")
	@RolesAllowed({"USER","ADMIN"})
	public String user() {
		return "<h1>Welcome User</h1>";
	}
	
	@RequestMapping("/admin")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//	@RolesAllowed("ADMIN")
	public String admin() {
		return "<h1>Welcome Admin</h1>";
	}
	
//	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
//	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//
//		try {
//			authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//			);
//		}
//		catch (BadCredentialsException e) {
//			throw new Exception("Incorrect username or password", e);
//		}
//
//
//		final UserDetails myUserDetails =  myUserDetailsService
//				.loadUserByUsername(authenticationRequest.getUsername());
//
//		final String jwt = jwtTokenUtil.generateToken(myUserDetails);
//
//		return ResponseEntity.ok(new AuthenticationResponse(jwt));
//	}
	 @PostMapping("/authenticate")
	 public String authenticateAndGetToken(@RequestBody AuthenticationRequest authRequest) {
		 
	     Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	     if (authentication.isAuthenticated()) {
	    	 return jwtService.generateToken(authRequest.getUsername());
	     } 
	     else {
	    	 throw new UsernameNotFoundException("invalid user request !");
	        }
	 }
}
