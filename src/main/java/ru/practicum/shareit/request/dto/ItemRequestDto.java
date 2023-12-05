package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    Long id;
    UserDto creator;
    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    String description;
    LocalDateTime created;
    List<ItemDto> items;
}
