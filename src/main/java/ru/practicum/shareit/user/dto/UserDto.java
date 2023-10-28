package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;
import ru.practicum.shareit.validation.validationInterface.UpdateValidationObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UserDto {
    Long id;

    @NotBlank(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    String name;

    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Email(groups = {CreateValidationObject.class, UpdateValidationObject.class}, message = "Поле должно содержать email")
    String email;
}
