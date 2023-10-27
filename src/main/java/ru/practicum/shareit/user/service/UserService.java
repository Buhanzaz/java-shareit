package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto dto);

    UserDto updateUser(Long userId, UserDto dto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUserById(Long userId);
}
