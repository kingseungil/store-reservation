package com.store;

import static com.store.type.UserRole.ROLE_ADMIN;

import com.store.entity.Authority;
import com.store.entity.Manager;
import com.store.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final ManagerRepository managerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (managerRepository.findByUsername("admin").isEmpty()) {
            managerRepository.save(Manager.builder()
                                          .username("admin")
                                          .password(passwordEncoder.encode("admin"))
                                          .activated(true)
                                          .authority(Authority.builder()
                                                              .authorityName(ROLE_ADMIN)
                                                              .build())
                                          .build());
        }
    }
}
