package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    Validation validation;
    UserMapper userMapper;


    @Override
    public UserDto addUser(UserDto dto) {
        User user = userMapper.ToModel(dto);

        validation.checkEmail(user.getEmail());
        return userMapper.ToDto(userRepository.addUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto dto) {
        User user = userMapper.ToModel(dto);

        validation.checkUserId(userId);
        user.setId(userId);
        validation.checkEmailForUpdate(userId, user.getEmail());
        return userMapper.ToDto(userRepository.updateUser(user));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.getUserById(userId);

        return userMapper.ToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> usersList = userRepository.getAllUsers();
        return usersList.stream().map(userMapper::ToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long userId) {
        validation.checkUserId(userId);
        userRepository.deleteUserById(userId);
    }
}
