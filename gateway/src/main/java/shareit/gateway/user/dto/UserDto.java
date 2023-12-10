package shareit.gateway.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import shareit.gateway.validation.interfaces.CreateValidationObject;
import shareit.gateway.validation.interfaces.UpdateValidationObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Size(max = 255, groups = {CreateValidationObject.class, UpdateValidationObject.class}, message = "Вы привыслили лимит в 255 символов")
    String name;

    @Email(groups = {CreateValidationObject.class, UpdateValidationObject.class}, message = "Поле должно содержать email")
    @NotEmpty(groups = {CreateValidationObject.class}, message = "Поле должно содержать email")
    @Size(max = 255, groups = {CreateValidationObject.class, UpdateValidationObject.class}, message = "Вы привыслили лимит в 255 символов")
    String email;
}
