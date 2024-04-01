package com.lcwd.electronic.store.dtos;

import org.apache.logging.log4j.util.StringBuilderFormattable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
	
	private String email;
	private String password;

}
