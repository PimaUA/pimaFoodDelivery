package com.pimaua.auth.repository;

import com.pimaua.auth.entity.Users;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class UsersRepositoryTest{
    @Autowired
    UsersRepository usersRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testSaveAndFindUsers() {
        Users user = Users.builder()
                .email("abc@com")
                .password("1234")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Users savedUser = usersRepository.save(user);

        Optional<Users> foundUser = usersRepository.findByEmail("abc@com");
        assertTrue(foundUser.isPresent());
        assertEquals("abc@com", foundUser.get().getEmail());

    }
}
