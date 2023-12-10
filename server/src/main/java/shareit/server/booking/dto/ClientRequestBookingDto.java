package shareit.server.booking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientRequestBookingDto {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
