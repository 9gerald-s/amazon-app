package com.jack.amazon.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

	@Value("${jwt.secret}")
	private String secret;
	@Value("&{jwt.expirationMs}")
	private long expirationMs;

	public String generateJwtToken(UserDetails userDetails) {
		Claims claims = Jwts.claims().setIssuedAt(new Date(System.currentTimeMillis()))
				.setSubject(userDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs));
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	public String getUserNameFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Claims generateClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	public boolean verifyJwtToken(String token, UserDetails userDetails) {
		Claims claims = generateClaimsFromToken(token);
		String userName = claims.getSubject();
		boolean isNotExpired = claims.getExpiration().after(new Date());
		
		return userName.equals(userDetails.getUsername()) && isNotExpired;
	}
}
