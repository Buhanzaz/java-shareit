package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    Long id; //Id вещи.

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    String name; //Название вещи.

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    String description; //Описание.

    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    Boolean available;//Статус доступности вещи True - доступно, False - нет.

    Long ownerId; //Владелец вещи ownerId == userId.

    Boolean isRequest; //True - вещь создана другим пользователем, False - владельцем вещи.

    Set<Long> reviews; //Собраны id отзывов.
}
