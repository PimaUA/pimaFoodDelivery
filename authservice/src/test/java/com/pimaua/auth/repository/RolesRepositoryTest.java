package com.pimaua.auth.repository;

import com.pimaua.auth.entity.Roles;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class RolesRepositoryTest{
    @Autowired
    RolesRepository rolesRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

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
