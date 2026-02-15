package com.evelyn.habit_tracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableScheduling
public class HabitTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitTrackerApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UsuarioRepository repository) {
		return args -> {
			if (repository.findByEmail("admin@teste.com").isEmpty()) {
				Usuario user = new Usuario();
				user.setNome("Evelyn");
				user.setEmail("admin@teste.com");
				user.setSenha("{noop}1234"); 
				repository.save(user);
				System.out.println("✅ Usuário de teste criado: admin@teste.com / 1234");
			}
		};
	}
} 