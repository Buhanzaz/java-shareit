package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    void deleteUserById(Long userId);
}
