package shareit.server.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import shareit.server.booking.enums.Status;
import shareit.server.item.dto.ItemDto;
import shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
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
