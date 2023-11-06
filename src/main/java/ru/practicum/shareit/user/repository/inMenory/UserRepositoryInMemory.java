package ru.practicum.shareit.user.repository.inMenory;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepositoryInMemory {
    User addUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    void deleteUserById(Long userId);
}
