package com.lcwd.electronic.store.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.websphere.WebSpherePreAuthenticatedWebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.*;


@Slf4j
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter{
	
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired 
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		// authentication
		
		String requestHeader = request.getHeader("Authorization");
		// Bearer  1626732798198u92y812y
		logger.info("Header : {} " + requestHeader);
		
		String username =null;
		String token = null;
		
		if(requestHeader!=null  && requestHeader.startsWith("Bearer")) {
			token = requestHeader.substring(7);
			
			try {
				
				username =this.jwtHelper.getUsernameFromToken(token);
				
			} catch (IllegalArgumentException e) {
			
				logger.info("Illegal Argument while fetching the username");
				e.printStackTrace();
			}catch (ExpiredJwtException e) {
				logger.info("Given token is Expired");
				e.printStackTrace();
				
			}catch (MalformedJwtException e) {
				// TODO: handle exception
				logger.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}else {
			logger.info("Invalid Token!!");
		}
		
		
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			// fetch user detail from username
			
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			Boolean validToken=this.jwtHelper.validateToken(token, userDetails);
			
			if(validToken) {
				
				// set the authentication
				
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}else {
				logger.info("Validation failed");
			}
			
			
		}
		
		
		filterChain.doFilter(request, response);
		
		
		
		
		
	}
	

}
