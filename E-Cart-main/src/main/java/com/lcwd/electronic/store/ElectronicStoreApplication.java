package com.lcwd.electronic.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.lcwd.electronic.store.models.Role;
import com.lcwd.electronic.store.repositories.RoleRepository;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;



@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Electronic Store",
				description = "Electronic Store REST API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Kunal Dhanwant",
						email = "kunaldhanwant8851@gmail.com",
						url = "https://www.linkedin.com/in/kunal-dhanwant-914878241/"
				)
						
		)
				
)



public class ElectronicStoreApplication implements CommandLineRunner {
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Value("${admin.role.id}")
	private String admin_role_id;
	
	@Value("${normal.role.id}")
	private String normal_role_id;
	
	
	
	

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
		
		
		
		
	}
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println(passwordEncoder.encode("123456"));
		
		
         try {
			
			Role normal_role = Role.builder().roleName("ROLE_NORMAL").roleId(normal_role_id).build();
			Role admin_role  = Role.builder().roleId(admin_role_id).roleName("ROLE_ADMIN").build();
			
			roleRepository.save(normal_role);
			roleRepository.save(admin_role);
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
