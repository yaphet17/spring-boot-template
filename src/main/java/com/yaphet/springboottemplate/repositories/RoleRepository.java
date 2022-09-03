package com.yaphet.springboottemplate.repositories;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(String roleName);

    @Query(value = "SELECT COUNT(*) FROM appuser_roles ar WHERE ar.role_id=?1 LIMIT ?2", nativeQuery = true)
    int findByRole(long roleId, int limit);

}
