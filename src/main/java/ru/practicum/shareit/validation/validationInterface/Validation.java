package ru.practicum.shareit.validation.validationInterface;

import org.springframework.stereotype.Component;

@Component
public interface Validation {
    void checksEmail(String userEmail);

    void checksUserId(Long userId);

    void checksEmailForUpdate(Long userId, String email);

    void checksItemId(Long itemId);

    void checksItemOwnership(Long itemId, Long userid);

}
