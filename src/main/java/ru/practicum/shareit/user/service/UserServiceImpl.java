package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.validationInterface.Validation;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    Validation validation;
    UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto dto) {
        User user = userMapper.toModel(dto);

        validation.checksEmail(user.getEmail());

        return userMapper.toDto(userRepository.addUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto dto) {
        User user = getUserByIdNotDto(userId);

        validation.checksEmailForUpdate(userId, dto.getEmail());

        return userMapper.toDto(userMapper.updateUser(user, dto));
    }

    @Override
    public UserDto getUserById(Long userId) {
        validation.checksUserId(userId);

        User user = userRepository.getUserById(userId);

        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> usersList = userRepository.getAllUsers();

        return usersList.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long userId) {
        validation.checksUserId(userId);
        userRepository.deleteUserById(userId);
    }

    private User getUserByIdNotDto(Long userId) {
        validation.checksUserId(userId);

        return userRepository.getUserById(userId);
    }

}
