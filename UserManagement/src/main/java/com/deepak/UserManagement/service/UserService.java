package com.deepak.UserManagement.service;

import com.deepak.UserManagement.entity.UserDetail;

public interface UserService {

	public UserDetail createUser(UserDetail User,String url);
	public boolean existsByEmail(String email);
	public void sendVerification(UserDetail user,String URL);
	public boolean verifyAccount(String code);
}
