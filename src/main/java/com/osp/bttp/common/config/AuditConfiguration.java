package com.osp.bttp.common.config;

import com.osp.bttp.dao.model.entity.db3.AccUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Profile("!test")
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userLogin != null && userLogin.getUsername() != null ? Optional.of(userLogin.getUsername()) : Optional.empty();
        }
    }

}
