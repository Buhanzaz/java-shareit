package ru.practicum.shareit.user.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryUserRepositoryImpl implements UserRepository {

    final HashMap<Long, User> users = new HashMap<>();

    Long id = 1L;

    @Override
    public User addUser(User user) {
        user.setId(id++);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public boolean isEmailInBd(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean isEmailInBdForUpdate(Long userId, String email) {
        return users.values().stream()
                .filter(user -> user.getId() != userId)
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean isUserInDb(Long userId) {
        Optional<User> user = Optional.ofNullable(users.get(userId));
        return user.isPresent();
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
