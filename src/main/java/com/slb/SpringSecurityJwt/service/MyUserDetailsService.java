package com.slb.SpringSecurityJwt.service;


import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.slb.SpringSecurityJwt.model.MyUserDetails;
import com.slb.SpringSecurityJwt.model.User;
import com.slb.SpringSecurityJwt.repo.UserRepository;




@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> user=userRepo.findByUserName(userName);
		user.orElseThrow(()-> new UsernameNotFoundException("Not found :"+userName));
		return user.map(MyUserDetails::new).get();
//		return new User("foo","foo",new ArrayList<>());
	}

}
