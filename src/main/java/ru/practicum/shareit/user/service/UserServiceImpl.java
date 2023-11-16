package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addUser(UserDto dto) {
        return userMapper.toDto(userRepository.save(userMapper.toModel(dto)));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Юзер с id " + userId + " не найден")
        );
        userMapper.updateUser(user, dto);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Юзер с id " + userId + " не найден")
        );

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
