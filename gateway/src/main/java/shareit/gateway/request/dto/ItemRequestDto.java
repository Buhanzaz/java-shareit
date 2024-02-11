package shareit.gateway.request.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import shareit.gateway.item.dto.ItemDto;
import shareit.gateway.user.dto.UserDto;
import shareit.gateway.validation.interfaces.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    Long id;
    UserDto creator;
    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Size(max = 255, groups = {CreateValidationObject.class}, message = "Вы привыслили лимит в 255 символов")
    String description;
    LocalDateTime created;
    List<ItemDto> items;
}
