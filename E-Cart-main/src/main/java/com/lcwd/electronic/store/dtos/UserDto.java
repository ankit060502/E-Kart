package com.lcwd.electronic.store.dtos;

import java.util.HashSet;
import java.util.Set;

import com.lcwd.electronic.store.models.Role;
import com.lcwd.electronic.store.models.User;
import com.lcwd.electronic.store.validate.UserImageValid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class UserDto {

	
	private String userId;
	
	@Size(min=3,message ="invalid name!!")
	private String name;
	
	//@Email(message="invalid user email")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",message =" invalid user email!!")
	@NotBlank(message="email should not be empty")
	private String email;
	
	@NotBlank(message="password is required!!")
	private String password;
	
	@NotBlank(message="fill  the gender")
	private String gender;
     
     @Size(max=1000,message ="message  length should not more than 1000")
	 private String about;
     
     @UserImageValid
	 private String userImage;
     
     private Set<RoleDto> roles = new HashSet<>();

}
