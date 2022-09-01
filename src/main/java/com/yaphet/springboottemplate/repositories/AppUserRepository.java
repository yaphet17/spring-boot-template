package com.yaphet.springboottemplate.repositories;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.utilities.AuthenticationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Query("SELECT a FROM AppUser a WHERE a.email=?1 AND a.deleted=FALSE ")
    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Query("UPDATE AppUser a SET a.enabled=TRUE WHERE a.email=?1")
    void enableAppUser(String email);

    @Modifying
    @Query("UPDATE AppUser a SET a.authType = ?2 WHERE a.userName = ?1")
    void updateAuthType(String userName, AuthenticationType authType);

    List<AppUser> findAllByEnabledIsFalse();

    @Modifying
    @Query("DELETE FROM AppUser a WHERE a.enabled = FALSE AND a.createdAt < ?1")
    int deleteAllUnverifiedUsers(LocalDateTime dateTime);
}

