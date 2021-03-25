package com.hubert.momoservice;

import com.hubert.momoservice.entity.Role;
import com.hubert.momoservice.entity.User;
import com.hubert.momoservice.entity.UserDetail;
import com.hubert.momoservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MomoserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MomoserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository) {
		Role userRole = new Role(Role.RoleType.USER);
		Role merchant = new Role(Role.RoleType.MERCHANT);

		User me = new User("me@gmail.com", "123456");
		UserDetail meDetail = new UserDetail(
				"Francis",
				"Ampong",
				"",
				"plt 24 B",
				"Ashanti",
				"Kumasi",
				"Mamponteng"
				);

		meDetail.setUser(me);
		me.getRoles().addAll(List.of(userRole, merchant));

		User lisa = new User("lisa@gmail.com", "123456");
		UserDetail lisaDetail = new UserDetail(
				"Lisa",
				"Lisa",
				"",
				"plt 90 G",
				"Ashanti",
				"Kumasi",
				"Macro"
		);

		lisaDetail.setUser(lisa);
		lisa.getRoles().add(userRole);

		User kwaku = new User("kwaku@gmail.com", "123456");
		UserDetail kwakuDetail = new UserDetail(
				"Kwaku",
				"Hubert",
				"",
				"plt 80 f",
				"Ashanti",
				"Kumasi",
				"Santasi"
		);

		kwakuDetail.setUser(kwaku);
		kwaku.getRoles().addAll(List.of(userRole, merchant));

		return args -> {
			userRepository.saveAll(List.of(me, lisa, kwaku));

			Iterable<User> users = userRepository.findAll();

//			users.forEach(user -> {
//
//			});

		};
	}
}
