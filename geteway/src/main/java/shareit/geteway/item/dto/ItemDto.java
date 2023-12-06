package shareit.geteway.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;
import org.hibernate.validator.constraints.Length;
import shareit.geteway.booking.dto.BookingWithoutItemDto;
import shareit.geteway.validation.CreateValidationObject;

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

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    String name; //Название вещи.

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Length(max = 1000, groups = {CreateValidationObject.class}, message = "Вы привыслили лимит в 1000 символов")
    String description; //Описание.

    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    Boolean available;//Статус доступности вещи True - доступно, False - нет.

    User user; //Владелец вещи

    Long requestId;

    BookingWithoutItemDto lastBooking;

    BookingWithoutItemDto nextBooking;

    List<CommentDto> comments;
}
