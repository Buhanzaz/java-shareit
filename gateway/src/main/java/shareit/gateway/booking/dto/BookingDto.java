package shareit.gateway.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import shareit.gateway.booking.enums.Status;
import shareit.gateway.item.dto.ItemDto;
import shareit.gateway.user.dto.UserDto;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemDto item;
    UserDto booker;
    Status status;
}
