package com.jack.amazon.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jack.amazon.entity.User;
import com.jack.amazon.repository.UserRepository;
import com.jack.amazon.utils.JwtUtils;

public class JwtService implements UserDetailsService {

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		try {
			user = userRepository.findById(username).get();
		} catch (UsernameNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				getAuthorities(user));
	}

	public Set<SimpleGrantedAuthority> getAuthorities(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));
		return authorities;
	}
	
	private void authenticate(String userName,String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
