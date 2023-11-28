package com.zb;

import static com.zb.type.UserRole.ROLE_ADMIN;

import com.zb.entity.Authority;
import com.zb.entity.Manager;
import com.zb.repository.ManagerRepository;
import com.zb.repository.queryDsl.ManagerQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final ManagerQueryRepository managerQueryRepository;
    private final ManagerRepository managerRepository;

    // Application이 실행되면서 admin 계정이 없으면 생성
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (managerQueryRepository.findByUsername("admin").isEmpty()) {
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
