package com.zb;

import com.zb.entity.Authority;
import com.zb.repository.AuthorityRepository;
import com.zb.type.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AuthorityInitializer {

    @Bean
    @Profile("test")
    public CommandLineRunner initAuthorities(AuthorityRepository authorityRepository) {
        // Application이 실행되면서 Authority가 없으면 생성
        return args -> {
            if (authorityRepository.count() == 0) {
                authorityRepository.save(new Authority(UserRole.ROLE_CUSTOMER));
                authorityRepository.save(new Authority(UserRole.ROLE_MANAGER));
            }
        };
    }
}
