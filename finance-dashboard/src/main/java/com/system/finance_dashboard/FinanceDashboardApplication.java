package com.system.finance_dashboard;

import com.system.finance_dashboard.entity.Role;
import com.system.finance_dashboard.entity.User;
import com.system.finance_dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class FinanceDashboardApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

	static void main(String[] args) {
		SpringApplication.run(FinanceDashboardApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = User.builder().name("Admin").username("admin").email("admin@gmail.com").password(passwordEncoder.encode("Admin@123"))
                            .role(Role.ADMIN).build();
            userRepository.save(admin);

            User analyst = User.builder().name("Analyst").username("analyst").email("analyst@gmail.com").password(passwordEncoder.encode("Analyst@123"))
                    .role(Role.ANALYST).build();
            userRepository.save(analyst);
        }
    }
}
