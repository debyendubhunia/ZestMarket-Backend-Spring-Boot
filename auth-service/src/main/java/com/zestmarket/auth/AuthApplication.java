package com.zestmarket.auth;

import com.zestmarket.auth.entity.ERole;
import com.zestmarket.auth.entity.Role;
import com.zestmarket.auth.entity.User;
import com.zestmarket.auth.repository.RoleRepository;
import com.zestmarket.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                       RoleRepository roleRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_ADMIN)));

            Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_CUSTOMER)));

            String adminEmail = "admin@zestmarket.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User(
                        adminEmail,
                        passwordEncoder.encode("admin123"),
                        "Admin",
                        "System",
                        "+1234567890"
                );
                admin.setRoles(Set.of(adminRole, customerRole));
                userRepository.save(admin);
                System.out.println(">>> Default Admin user created: email=" + adminEmail + ", password=admin123 <<<");
            }
        };
    }
}
