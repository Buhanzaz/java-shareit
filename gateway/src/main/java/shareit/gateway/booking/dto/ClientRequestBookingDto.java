package shareit.gateway.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import shareit.gateway.validation.annotation.StartBeforeEndDateValid;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@StartBeforeEndDateValid
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
