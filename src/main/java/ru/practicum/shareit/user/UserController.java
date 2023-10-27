package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;
import ru.practicum.shareit.validation.validationInterface.UpdateValidationObject;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    private final String URI_ID_USER = "/{userId}";

    @PostMapping
    public ResponseEntity<UserDto> postRequestUser(@RequestBody @Validated(CreateValidationObject.class) UserDto dto) {
        UserDto userDto = userService.addUser(dto);
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping(path = URI_ID_USER)
    public ResponseEntity<UserDto> patchRequestUser(@PathVariable Long userId, @RequestBody @Validated(UpdateValidationObject.class) UserDto dto) {
        UserDto userDto = userService.updateUser(userId, dto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = URI_ID_USER)
    public ResponseEntity<UserDto> getRequestUser(@PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getRequestAllUsers() {
        List<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }

    @DeleteMapping(path = URI_ID_USER)
    public void deleteRequestUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
