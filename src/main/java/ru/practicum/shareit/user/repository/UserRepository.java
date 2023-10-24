package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface UserRepository {

    User addUser(User user);

    boolean isEmailInBd(String email);


    boolean isEmailInBdForUpdate(Long userId, String email);

    User updateUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    void deleteUserById(Long userId);

    boolean isUserInDb(Long userId);

}
