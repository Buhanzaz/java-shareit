package ru.practicum.shareit.user.repository.inMenory;

public interface ValidationUserRepositoryInMemory {
    boolean isEmailInBd(String email);

    boolean isEmailInBdForUpdate(Long userId, String email);

    boolean isUserInDb(Long userId);
}

