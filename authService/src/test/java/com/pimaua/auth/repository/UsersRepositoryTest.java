package com.pimaua.auth.repository;

import com.pimaua.auth.entity.Users;
import com.pimaua.auth.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersRepositoryTest extends BaseRepositoryTest {
    @Autowired
    UsersRepository usersRepository;

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
