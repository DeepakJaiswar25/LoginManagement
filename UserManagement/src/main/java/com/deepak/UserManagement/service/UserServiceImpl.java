package com.deepak.UserManagement.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.deepak.UserManagement.entity.UserDetail;
import com.deepak.UserManagement.repository.UserRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepo;
	@Autowired
	JavaMailSender mailSender;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Override
	public UserDetail createUser(UserDetail user,String url) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		user.setEnabled(false);
		UUID randomcode= UUID.randomUUID();
		user.setVerificationCode(randomcode.toString());
		UserDetail detail = userRepo.save(user);
		sendVerification(user, url);
		return detail;
	}
	@Override
	public boolean existsByEmail(String email) {
	
		return userRepo.existsByEmail(email);
	}
	
	
	@Override
	public void sendVerification(UserDetail user, String URL) {
		
		String from="deepakj9725@gmail.com";
		String to=user.getEmail();
		String subject="Account Verification";
		String content="Dear [[name]],<br>"
				+ "please click the link below to verify your registration: <br>"
				+ "<h3><a href='[[URL]]'>VERIFY</a></h3>"
				+ "Thank You, <br>"
				+ "Deepak";
//	   
		try {
			 MimeMessage message= mailSender.createMimeMessage();
			 MimeMessageHelper helper= new MimeMessageHelper(message);
			 helper.setFrom(from);
			 helper.setTo(to);
			 helper.setSubject(subject);
			 String siteurl=URL+"/verify?code="+user.getVerificationCode();
			 System.out.println(siteurl);
			 content=content.replace("[[name]]", user.getFullName());
			 content=content.replace("[[URL]]", siteurl);
			 helper.setText(content,true);
			 mailSender.send(message);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
	@Override
	public boolean verifyAccount(String code) {
		System.out.println(code);
		 UserDetail detail = userRepo.findByVerificationCode(code);
		 if(detail!=null) {
			 detail.setEnabled(true);
			 detail.setVerificationCode(null);
			 userRepo.save(detail);
			 return true;
		 }
		 else {
			 return false;
		 }
	}

}
