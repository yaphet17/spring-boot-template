package com.yaphet.springboottemplate.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yaphet.springboottemplate.repositories.PersistentLoginRepository;

@Service
public class PersistentLoginService {

    private final PersistentLoginRepository persistentLoginRepository;
    private final int tokenValiditySeconds;

    public PersistentLoginService(PersistentLoginRepository persistentLoginRepository,
                                  @Value("${app.spring-boot-template.remember-me.token-validity-seconds}") int tokenValiditySeconds) {
        this.persistentLoginRepository = persistentLoginRepository;
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    @Transactional
    public int removeAllExpiredTokens() {
        return persistentLoginRepository.deleteAllExpiredTokens(LocalDateTime.now().minusSeconds(tokenValiditySeconds));
    }
}
