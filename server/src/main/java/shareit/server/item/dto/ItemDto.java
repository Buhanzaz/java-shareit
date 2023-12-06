package shareit.server.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import shareit.server.booking.dto.BookingWithoutItemDto;
import shareit.server.user.model.User;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id; //Id вещи.

    String name; //Название вещи.

    String description; //Описание.

    Boolean available;//Статус доступности вещи True - доступно, False - нет.

    User user; //Владелец вещи

    Long requestId;

    BookingWithoutItemDto lastBooking;

    BookingWithoutItemDto nextBooking;

    List<CommentDto> comments;
}
