package com.deepak.UserManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deepak.UserManagement.entity.UserDetail;
import com.deepak.UserManagement.repository.UserRepository;
import com.deepak.UserManagement.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class homeController {
	
	@Autowired
	UserService service;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userrepo;
	
	
	@GetMapping("/")
	public String index() {
		return "index";	
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";	
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";	
	}

	@GetMapping("/forgotPass")
	public String loadForgotPassword() {
		
		return "forgotPassword";
	}
	@GetMapping("/resetPass/{id}")
	public String loadResetPassword(@PathVariable int id,Model m) {
		m.addAttribute("id",id);
		return "resetPassword";
	}
	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email, @RequestParam String mobileNumber,RedirectAttributes redirectAttributes) {
		UserDetail detail = userrepo.findByEmailAndMobileNumber(email, mobileNumber);
		if(detail!=null) {
			return "redirect:/resetPass/"+detail.getId();
		}
		else {
			redirectAttributes.addFlashAttribute("msg", "Incorrect EmailId and Mobile Number");
		}
		return "redirect:/forgotPass";
	}
	@PostMapping("/resetPassword")
	public String resetPassword(RedirectAttributes redirectAttributes, @RequestParam String newPassword,@RequestParam String confirmPassword, @RequestParam int id) {
		 UserDetail detail = userrepo.findById(id).get();
		 if (!newPassword.equals(confirmPassword)) {
		        redirectAttributes.addFlashAttribute("msg1", "Passwords do not match!");
		        return "redirect:/resetPass/"+id; 
		    }
		 if(detail!=null) {
			 detail.setPassword(passwordEncoder.encode(newPassword));
			userrepo.save(detail);
			redirectAttributes.addFlashAttribute("msg", "Password reset successfully");
			return "redirect:/forgotPass";
			}
			else {
				redirectAttributes.addFlashAttribute("msg", "Error occured");
			}
		return "forgotPassword";
	}
	
	@GetMapping("/verify")
	public String verify(@RequestParam("code") String Code) {
		boolean account = service.verifyAccount(Code);
		account =true;
		if(account) {
			return "success";
		}
		else {
			return "failed";
		}
	}
	
	@PostMapping("/createUser")
	public String createUser(@ModelAttribute UserDetail user, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		 String url=request.getRequestURL().toString();
		 url=url.replace(request.getServletPath(), "");
		String email=((UserDetail) user).getEmail();
		boolean Exist= service.existsByEmail(email);
		if(Exist) {
			redirectAttributes.addFlashAttribute("msg", "Email-Id Already exists!!");
		}
		else {
			
			UserDetail details = service.createUser(user,url);
//			System.out.println(details);
			redirectAttributes.addFlashAttribute("msg", "Registered Sucessfully");
		}
		return "redirect:/register";
		
	}
}
