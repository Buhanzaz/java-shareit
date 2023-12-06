package shareit.geteway.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import shareit.geteway.item.dto.ItemDto;
import shareit.geteway.user.dto.UserDto;
import shareit.geteway.validation.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
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
