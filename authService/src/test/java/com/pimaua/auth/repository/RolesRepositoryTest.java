package com.pimaua.auth.repository;

import com.pimaua.auth.entity.Roles;
import com.pimaua.auth.test.utils.BaseRepositoryTest;
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
