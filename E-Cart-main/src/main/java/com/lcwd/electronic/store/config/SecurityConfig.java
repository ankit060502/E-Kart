package com.lcwd.electronic.store.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.catalina.filters.CorsFilter;
import org.aspectj.weaver.tools.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.AbstractDaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.lcwd.electronic.store.security.JwtAuthenticationFilter;




@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jAuthenticationFilter;
	
	
	private final String[]  pUBLIC_URL = {
		"/swagger-ui/**",
		"/webjars/**",
		"/swagger-resources/**",
		"/v3/api-docs/**",
		"/api/v1/auth/**"
			
	};
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		
//		UserDetails normal =User.builder().username("ANKIT")
//				.password(passwordEncoder().encode("ankit"))
//				.roles("NORMAL")
//				
//				.build();
//		
//		
//		UserDetails  admin  =User.builder().username("KUNAL")
//				.password(passwordEncoder().encode("kunal"))
//				.roles("ADMIN")
//				.build();
//		
//		
//		return new InMemoryUserDetailsManager(normal,admin);
//	}
//	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
	
	
//	@Bean
//	public FilterRegistrationBean corsFilter() {
//		
//		UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowCredentials(true);
//		
//		
//		configuration.addAllowedOriginPattern("*");
//		configuration.addAllowedHeader("Authorization");
//		configuration.addAllowedHeader("Content-Type");
//		configuration.addAllowedHeader("Accept");
//		configuration.addAllowedMethod("GET");
//		configuration.addAllowedMethod("POST");
//		configuration.addAllowedMethod("DELETE");
//		configuration.addAllowedMethod("PUT");
//		configuration.addAllowedMethod("OPTIONS");
//		
//		configuration.setMaxAge(3600L);
//		source.registerCorsConfiguration("/**", configuration);
//		
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter());
//		
//		filterRegistrationBean.setOrder(-110);
//		return filterRegistrationBean;
//		
//		
//	}
	
	@Bean
	public FilterRegistrationBean corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.setAllowedOrigins(Collections.singletonList("*"));
	    config.setAllowedHeaders(Arrays.asList("Authorization","Origin", "Content-Type", "Accept"));
	    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
	    source.registerCorsConfiguration("/**", config);
	    FilterRegistrationBean registration = new FilterRegistrationBean(new CorsFilter());
	    return registration;
	}
	
	
	
	// basic authentication
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		
//		httpSecurity.csrf(csrf->csrf.disable())
//		.cors(cors-> cors.disable())
//		.authorizeHttpRequests(auth->
//		         auth.requestMatchers("/auth/login").permitAll().anyRequest().authenticated()).exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint))
//		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//		httpSecurity.addFilterBefore(jAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//		
//		return httpSecurity.build();
		
		httpSecurity.csrf(csrf->csrf.disable())
		
		.authorizeHttpRequests(auth->
		         auth.requestMatchers("/auth/login").permitAll().requestMatchers(HttpMethod.POST,"/users").permitAll().requestMatchers(pUBLIC_URL).permitAll().requestMatchers("/auth/google").permitAll().anyRequest().authenticated()).exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint))
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.addFilterBefore(jAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return httpSecurity.build();
	}
	
	
	
	
	
	
	
	
	

	
	

}
