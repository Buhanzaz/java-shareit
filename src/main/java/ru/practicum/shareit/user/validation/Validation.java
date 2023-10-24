package ru.practicum.shareit.user.validation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Validation {

    UserRepository userRepository;

    public void checkEmail(String userEmail) {
        if (userRepository.searchEmailValidUser(userEmail)) {
            throw new ValidationException("Email занят");
        }
    }

    public void checkUserId(Long userId) {
        if (!userRepository.searchIdValidUser(userId)) {
            throw new ValidationException("Пользователя с таким id не существует");
        }
    }

    public void checkEmailForUpdate(Long userId, String email) {
        if (userRepository.searchEmailValidUserForUpdate(userId, email)) {
            throw new ValidationException("Email занят");
        }
    }
}
