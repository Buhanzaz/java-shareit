package shareit.gateway.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import shareit.gateway.validation.interfaces.CreateValidationObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    @NotBlank(groups = {CreateValidationObject.class}, message = "Вы привыслили лимит в 255 символов")
    @Size(max = 255, groups = {CreateValidationObject.class}, message = "Вы привыслили лимит в 255 символов")
    String text;
    String authorName;
    LocalDateTime created;
}
