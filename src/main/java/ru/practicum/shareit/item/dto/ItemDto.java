package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.booking.dto.BookingWithoutItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@Builder
@EqualsAndHashCode
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
