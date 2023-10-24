package ru.practicum.shareit.validation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.validationInterface.Validation;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValidationImpl implements Validation {

    UserRepository userRepository;
    ItemRepository itemRepository;

    @Override
    public void checksEmail(String userEmail) {
        if (userRepository.isEmailInBd(userEmail)) {
            throw new ConflictException("Email занят");
        }
    }

    @Override
    public void checksUserId(Long userId) {
        if (!userRepository.isUserInDb(userId)) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }

    @Override
    public void checksEmailForUpdate(Long userId, String email) {
        if (userRepository.isEmailInBdForUpdate(userId, email)) {
            throw new ConflictException("Email занят");
        }
    }

    @Override
    public void checksItemId(Long itemId) {
        if (!itemRepository.isItemInBd(itemId)) {
            throw new NotFoundException("Вещи с таким id не существует");
        }
    }

    @Override
    public void checksItemOwnership(Long itemId, Long userid) {
        if (!itemRepository.isItemOwner(itemId, userid)) {
            throw new NotFoundException("Вещь с таким id не принадлежит вам");
        }
    }

}
