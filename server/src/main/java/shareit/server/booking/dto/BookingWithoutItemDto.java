package shareit.server.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class BookingWithoutItemDto {
    Long id;
    Long bookerId;
    LocalDateTime start;
    LocalDateTime end;
}
