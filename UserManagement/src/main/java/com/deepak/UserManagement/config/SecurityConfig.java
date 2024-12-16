package com.deepak.UserManagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	UserDetailsService getUserDetailService() {
		return new UserDetailServiceImpl();
	}
	@Bean
	BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	AuthenticationSuccessHandler customSuccessHandler;
	
    public DaoAuthenticationProvider getdaoAuthenticationProvider() {
    	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    	daoAuthenticationProvider.setUserDetailsService(getUserDetailService());
    	daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
    	return daoAuthenticationProvider;
    	
    }
//	If we use websecurityconfigureradapter then we configure like this
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(getdaoAuthenticationProvider());
//	}
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		 
//		http.authorizeHttpRequests().antMatchers("/admin/**").hasRole("ADMIN")
//		.antMatchers("/user/**").hasRole("USER")
//		.antMatchers("/**").permitAll().and()
//		.formLogin().loginPage("/signin").loginProcessingUrl("/login")
//		.successHandler(customSuccessHandler).and().csrf().disable();
//	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/**").permitAll().and()
		.formLogin().loginPage("/signin").loginProcessingUrl("/login")
		.successHandler(customSuccessHandler).and().csrf().disable(); 
    	http.authenticationProvider(getdaoAuthenticationProvider());
    	return http.build();
    	
    	
    }
 
}
