package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args){
        if(roleRepository.findByName("ROLE_ADMIN")==null){
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
        if(roleRepository.findByName("ROLE_USER")==null){
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        if (!userRepository.findByUsername("admin").isPresent()){
            User admin =new User();
            admin.setUsername("admin");
            String encodedPassword = passwordEncoder.encode("admin");
            admin.setPassword(encodedPassword);
            admin.setEmail("admin_mail@gmail.com");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (adminRole != null) {
                admin.getRoles().add(adminRole);
            }
            if (userRole != null) {
                admin.getRoles().add(userRole);
            }
            userRepository.save(admin);
        }
        if (!userRepository.findByUsername("user").isPresent()) {
            User user = new User();
            user.setUsername("user");
            String encodedPasswordForUser = passwordEncoder.encode("user");
            user.setPassword(encodedPasswordForUser);
            user.setEmail("user_mail@gmail.com");
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole != null) {
                user.getRoles().add(userRole);
            }
            userRepository.save(user);

        }    }
}
