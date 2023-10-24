package ru.practicum.shareit.user.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryUserRepositoryImpl implements UserRepository {

    final HashMap<Long, User> users = new HashMap<>();

    Long GenerateUserId = 1L;

    @Override
    public User addUser(User user) {
        user.setId(GenerateUserId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean searchEmailValidUser(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean searchEmailValidUserForUpdate(Long userId, String email) {
        for (User user : users.values()) {
            if (user.getId() != userId) {
                if (user.getEmail().equals(email)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean searchIdValidUser(Long id) {
        return users.values().stream().anyMatch(user -> user.getId() == id);
    }

    @Override
    public User updateUser(User user) {
        User changeUser = getUserById(user.getId());
        Optional<String> name = Optional.ofNullable(user.getName());
        Optional<String> email = Optional.ofNullable(user.getEmail());

        name.ifPresent(changeUser::setName);
        email.ifPresent(changeUser::setEmail);

        return changeUser;
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }
}
