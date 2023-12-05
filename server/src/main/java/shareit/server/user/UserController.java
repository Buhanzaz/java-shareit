package shareit.server.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.server.user.dto.UserDto;
import shareit.server.user.service.UserService;
import shareit.server.validation.validationInterface.CreateValidationObject;
import shareit.server.validation.validationInterface.UpdateValidationObject;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping(path = "/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    private static final String URI_ID_USER = "/{userId}";

    @GetMapping
    public ResponseEntity<List<UserDto>> getRequestAllUsers() {
        List<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }

    @PostMapping
    public ResponseEntity<UserDto> postRequestUser(@RequestBody @Validated(CreateValidationObject.class) UserDto dto) {
        UserDto userDto = userService.addUser(dto);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping(path = URI_ID_USER)
    public void deleteRequestUser(@PathVariable @Min(1) Long userId) {
        userService.deleteUserById(userId);
    }

    @PatchMapping(path = URI_ID_USER)
    public ResponseEntity<UserDto> patchRequestUser(@PathVariable @Min(1) Long userId, @RequestBody @Validated(UpdateValidationObject.class) UserDto dto) {
        UserDto userDto = userService.updateUser(userId, dto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = URI_ID_USER)
    public ResponseEntity<UserDto> getRequestUser(@PathVariable @Min(1) Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

}
