package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientRequestBookingDto {
    @NotNull(message = "Поле itemId не может быть пустым")
    Long itemId;

    @FutureOrPresent(message = "Дата начала бронирования не может раньше чем сегодняшнее число")
    @NotNull(message = "Дата начала бронирования не может быть пуста")
    LocalDateTime start;

    @Future(message = "Дата начала бронирования не может раньше чем сегодняшнее число")
    @NotNull(message = "Дата окончания бронирования не может быть пуста")
    LocalDateTime end;
}
