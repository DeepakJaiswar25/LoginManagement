package com.deepak.UserManagement.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deepak.UserManagement.entity.UserDetail;
import com.deepak.UserManagement.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userrepo;
    @Autowired
	BCryptPasswordEncoder passwordEncoder;
	@ModelAttribute
	public void userdetails(Model m,Principal p) {
		String email = p.getName();
		UserDetail user = userrepo.findByEmail(email);
		m.addAttribute("user",user);
	}
	
	@GetMapping("/")
	public String home() {
		
		return "/user/home";
	}
	@GetMapping("/ChangePass")
	public String loadChangePassword() {
		
		return "/user/ChangePass";
	}
	
	
	@PostMapping("/updatePass")
	public String updatePassword(RedirectAttributes attributes,Principal p,@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass) {
		
		String email = p.getName();
		UserDetail user = userrepo.findByEmail(email);
		boolean matches = passwordEncoder.matches(oldPass, user.getPassword());
		if(matches) {
			user.setPassword(passwordEncoder.encode(newPass));
			UserDetail userDetail = userrepo.save(user);
			if(userDetail!=null) {
			attributes.addFlashAttribute("msg", "Password Changed Successfully");
		}
			else {
				attributes.addFlashAttribute("msg1", "Something Wrong in server");
			}
		}
		else {
			attributes.addFlashAttribute("msg1", "Old Password Wrong");
		}
		return "redirect:/user/ChangePass";
	}
}
