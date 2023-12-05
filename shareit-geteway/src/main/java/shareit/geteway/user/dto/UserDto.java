package shareit.geteway.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import shareit.geteway.validation.CreateValidationObject;
import shareit.geteway.validation.UpdateValidationObject;

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
