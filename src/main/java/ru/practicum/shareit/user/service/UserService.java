package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

@Service
public interface UserService {
    
    UserDto addUser(UserDto dto);

    UserDto updateUser(Long userId, UserDto dto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUserById(Long userId);
    
}
