package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Sql(scripts = {"file:src/main/resources/schema.sql"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceImplTest {

    final UserService userService;

    UserDto firstUserDto;
    UserDto secondUserDto;

    @BeforeEach
    void setUp() {
        firstUserDto = UserDto.builder()
                .name("TestNameOne")
                .email("TestEmail@One.com")
                .build();

        secondUserDto = UserDto.builder()
                .name("TestNameTwo")
                .email("TestEmail@Two.com")
                .build();
    }

    @Test
    void addUserAndGetUser() {
        UserDto createUser = userService.addUser(firstUserDto);
        UserDto getUser = userService.getUserById(createUser.getId());

        assertThat(createUser).usingRecursiveComparison().isEqualTo(getUser);
    }

    @Test
    void addUserErrorDuplicateEmail() {
        userService.addUser(firstUserDto);
        secondUserDto.setEmail(firstUserDto.getEmail());

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                () -> userService.addUser(secondUserDto));

        assertEquals("could not execute statement; SQL [n/a]; constraint [null]; " +
                "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                "could not execute statement", exception.getMessage());
    }

    @Test
    void updateUser() {
        UserDto createUser = userService.addUser(firstUserDto);
        UserDto updateUser = userService.updateUser(createUser.getId(), secondUserDto);
        UserDto getUser = userService.getUserById(updateUser.getId());

        assertThat(updateUser).usingRecursiveComparison().isEqualTo(getUser);
    }

    @Test
    void updateUserFailEmailExists() {
        userService.addUser(firstUserDto);
        userService.addUser(secondUserDto);
        secondUserDto.setEmail(firstUserDto.getEmail());

        InvalidDataAccessApiUsageException exception = assertThrows(InvalidDataAccessApiUsageException.class,
                () -> userService.updateUser(secondUserDto.getId(), secondUserDto));

        assertEquals("The given id must not be null!; nested exception is java.lang.IllegalArgumentException: The given id must not be null!", exception.getMessage());
    }

    @Test
    void updateUserNotFound() {
        firstUserDto.setId(1L);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.updateUser(firstUserDto.getId(), firstUserDto));

        assertEquals("Юзер с id " + firstUserDto.getId() + " не найден", exception.getMessage());
    }

    @Test
    void getAllUsers() {
        UserDto createUser1 = userService.addUser(firstUserDto);
        UserDto createUser2 = userService.addUser(secondUserDto);
        List<UserDto> getUsers = userService.getAllUsers();

        assertThat(getUsers.get(0)).usingRecursiveComparison().isEqualTo(createUser1);
        assertThat(getUsers.get(1)).usingRecursiveComparison().isEqualTo(createUser2);
    }

    @Test
    void deleteUserById() {
        UserDto createUser1 = userService.addUser(firstUserDto);
        userService.deleteUserById(createUser1.getId());

        assertThatThrownBy(() -> userService.getUserById(createUser1.getId())).isInstanceOf(NotFoundException.class);
    }
}