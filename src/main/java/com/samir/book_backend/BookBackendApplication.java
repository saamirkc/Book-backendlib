package com.samir.book_backend;

import com.samir.book_backend.role.Role;
import com.samir.book_backend.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableJpaAuditing
@EnableJpaAuditing(auditorAwareRef = "auditorAware")  //after adding  ApplicationAuditAware so that @CreateBy and @LastModifiedBY also works wih @Created Date and @LastModified Date
@EnableAsync
public class BookBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookBackendApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if(roleRepository.findByRoleName("USER").isEmpty()) {
                   roleRepository.save(
						   Role.builder().roleName("USER").build()
				   );
			}
		};
	}

}
