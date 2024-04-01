package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.exception.ImageBadApiRequest;
import com.lcwd.electronic.store.models.User;
import com.lcwd.electronic.store.security.JwtHelper;
import com.lcwd.electronic.store.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(
        name = "CRUD REST APIs for signup/login  "
        
)
public class AuthController {
	
	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtHelper helper;
	
	@Value("${newPassword}")
	private String newPassword;
	
	@Value("${googleClientId}")
	private String googleClientId;
	
	
	
	
	
	
	
	
	@Operation(
            summary = "Login REST API",
            description = "Login REST API is used to Login User"
    )
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
		this.doAuthenticate(request.getEmail(), request.getPassword());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		
		String token = this.helper.generateToken(userDetails);
		
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		JwtResponse response = JwtResponse.builder()
				.jwtToken(token).user(userDto).build();
		
		return new ResponseEntity<JwtResponse>(response,HttpStatus.OK);
	}
	
	
	
	
	
	
	private void doAuthenticate(String email ,String password) {
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
		
		try {
			authenticationManager.authenticate(authenticationToken);
			
		} catch (BadCredentialsException e) {
			// TODO: handle exception
			throw new ImageBadApiRequest("invalid username or password!!");
		}
	}
	
	
	
	@Operation(
            summary = "Info REST API",
            description = "Info REST API is used to fetch detail of loginUser User"
    )
	
	@GetMapping("/current")
	public ResponseEntity<UserDto>  getCurrentUser(Principal principal){
		
		String nameString = principal.getName();
		
		
		return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(nameString), UserDto.class),HttpStatus.OK);
		
		
	}
	
	
	//  login with google
	@Operation(
            summary = "Login Google REST API",
            description = "Login Google REST API is used to Login with Google"
    )
	
	@PostMapping("/google")
	public ResponseEntity<JwtResponse> loginWithGoogl(@RequestBody Map<String, Object> data) throws IOException{
		
		
		// get the id  token from google
		
		String idToken = data.get("idToken").toString();
		
		NetHttpTransport netHttpTransport = new NetHttpTransport();
		JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
		
		GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport,jacksonFactory).setAudience(Collections.singleton(googleClientId));
		
		
		
		GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
		
		GoogleIdToken.Payload payload = googleIdToken.getPayload();
		
		log.info("payload: {}",payload);
		
		String email = payload.getEmail();
		
		User user =null;
		
		user = userService.findUserByEmailOptional(email).orElse(null);
		
		if(user==null) {
			user = this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
			
		}
		
		ResponseEntity<JwtResponse> jwtResponseEntity = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
		return jwtResponseEntity;
		
		
		
				
		
	}






	private User saveUser(String email, String name, String photoUrl) {
		// TODO Auto-generated method stub
		
		UserDto newUser = UserDto.builder().name(name).userImage(photoUrl).email(email).password(newPassword)
				.roles(new HashSet<>()).build();
		
		return modelMapper.map(newUser, User.class);
	}

}
