package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.models.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
	
	
   private String roleId;
	
	private String roleName;
}
