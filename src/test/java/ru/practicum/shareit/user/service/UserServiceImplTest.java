package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Sql(scripts = {"file:src/main/resources/schema.sql"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceImplTest {

    final UserServiceImpl userService;

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
    void updateUser() {
        UserDto createUser = userService.addUser(firstUserDto);
        UserDto updateUser = userService.updateUser(createUser.getId(), secondUserDto);
        UserDto getUser = userService.getUserById(updateUser.getId());

        assertThat(updateUser).usingRecursiveComparison().isEqualTo(getUser);
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