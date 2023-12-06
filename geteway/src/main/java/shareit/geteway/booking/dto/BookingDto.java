package shareit.geteway.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import shareit.geteway.booking.enums.Status;
import shareit.geteway.item.dto.ItemDto;
import shareit.geteway.user.dto.UserDto;

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
