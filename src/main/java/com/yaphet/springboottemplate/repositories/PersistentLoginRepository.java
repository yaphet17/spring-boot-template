package com.yaphet.springboottemplate.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yaphet.springboottemplate.models.PersistentLogin;

@Repository
public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {

    @Modifying
    @Query(value = "DELETE FROM persistent_logins WHERE last_used < ?1", nativeQuery = true)
    int deleteAllExpiredTokens(LocalDateTime dateTime);
}
