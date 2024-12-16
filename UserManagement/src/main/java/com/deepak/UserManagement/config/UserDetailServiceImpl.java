package com.deepak.UserManagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deepak.UserManagement.entity.UserDetail;
import com.deepak.UserManagement.repository.UserRepository;
@Service
public class UserDetailServiceImpl implements UserDetailsService{

	@Autowired
	UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDetail user = userRepo.findByEmail(email);
		if(user!=null) {
			return new CustomUserDetails(user);
		}
		else {
			throw new UsernameNotFoundException("User not Found");
		}
	}

}
