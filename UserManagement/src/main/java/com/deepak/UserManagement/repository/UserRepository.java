package com.deepak.UserManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepak.UserManagement.entity.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail,Integer>{
	
	boolean existsByEmail(String email);
	UserDetail findByEmail(String email);
	UserDetail findByEmailAndMobileNumber(String email, String mobileNumber);
	UserDetail findByVerificationCode(String code);
}
