package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.validation.CreateValidationObject;
import ru.practicum.shareit.validation.UpdateValidationObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UserDto {
    long id;

    @NotBlank(groups = {CreateValidationObject.class},message = "Поле не может быть пустым")
    String name;

    @NotNull(groups = {CreateValidationObject.class}, message = "Поле не может быть пустым")
    @Email(groups = {CreateValidationObject.class, UpdateValidationObject.class},message = "Поле должно содержать email")
    String email;
}
