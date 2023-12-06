package shareit.gateway.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import shareit.gateway.validation.CreateValidationObject;
import shareit.gateway.validation.UpdateValidationObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    String name;

    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Email(groups = {CreateValidationObject.class, UpdateValidationObject.class}, message = "Поле должно содержать email")
    String email;
}
