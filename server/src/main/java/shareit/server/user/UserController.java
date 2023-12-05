package shareit.server.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.server.user.dto.UserDto;
import shareit.server.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
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
        log.info("Вход в server");
        List<UserDto> userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }

    @PostMapping
    public ResponseEntity<UserDto> postRequestUser(@RequestBody UserDto dto) {
        UserDto userDto = userService.addUser(dto);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping(path = URI_ID_USER)
    public void deleteRequestUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    @PatchMapping(path = URI_ID_USER)
    public ResponseEntity<UserDto> patchRequestUser(@PathVariable Long userId, @RequestBody UserDto dto) {
        UserDto userDto = userService.updateUser(userId, dto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = URI_ID_USER)
    public ResponseEntity<UserDto> getRequestUser(@PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

}
