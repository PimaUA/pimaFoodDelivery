package com.pimaua.authService.repository;

import com.pimaua.authService.entity.Roles;
import com.pimaua.authService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RolesRepositoryTest extends BaseRepositoryTest {
    @Autowired
    RolesRepository rolesRepository;

    @Test
    void testSaveAndFindRoles() {
      Roles role=Roles.builder()
              .roleName("ADMIN")
              .build();

        Roles savedRole = rolesRepository.save(role);

        Optional<Roles> foundRole = rolesRepository.findByRoleName("ADMIN");
        assertTrue(foundRole.isPresent());
        assertEquals("ADMIN", foundRole.get().getRoleName());
    }
}
