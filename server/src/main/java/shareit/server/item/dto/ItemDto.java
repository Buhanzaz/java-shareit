package shareit.server.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import shareit.server.booking.dto.BookingWithoutItemDto;
import shareit.server.user.model.User;
import shareit.server.validation.validationInterface.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
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
