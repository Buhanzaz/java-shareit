package ru.practicum.shareit.booking.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {
    ALL ("ALL"),  // Все
    CURRENT ("CURRENT"), // Текущие
    PAST ("PAST"), // Завершённые
    FUTURE ("FUTURE"), // Будущие
    WAITING ("WAITING"), // Ожидающие подтверждения
    REJECTED ("REJECTED"); // Отклонённые

    private final String state;
}
