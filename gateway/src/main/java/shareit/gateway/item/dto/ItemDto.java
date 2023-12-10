package shareit.gateway.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;
import shareit.gateway.validation.interfaces.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id; //Id вещи.

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Size(max = 255, groups = {CreateValidationObject.class}, message = "Вы привыслили лимит в 255 символов")
    String name; //Название вещи.

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Size(max = 255, groups = {CreateValidationObject.class}, message = "Вы привыслили лимит в 255 символов")
    String description; //Описание.

    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    Boolean available;//Статус доступности вещи True - доступно, False - нет.

    User user; //Владелец вещи

    Long requestId;

    BookingWithoutItemDto lastBooking;

    BookingWithoutItemDto nextBooking;

    List<CommentDto> comments;
}
