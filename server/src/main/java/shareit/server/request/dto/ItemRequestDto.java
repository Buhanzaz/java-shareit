package shareit.server.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import shareit.server.item.dto.ItemDto;
import shareit.server.user.dto.UserDto;
import shareit.server.validation.interfaces.CreateValidationObject;

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
