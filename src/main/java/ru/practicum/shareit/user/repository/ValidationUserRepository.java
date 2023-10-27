package ru.practicum.shareit.user.repository;

public interface ValidationUserRepository {
    boolean isEmailInBd(String email);

    boolean isEmailInBdForUpdate(Long userId, String email);

    boolean isUserInDb(Long userId);
}

