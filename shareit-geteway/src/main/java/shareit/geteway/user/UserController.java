package shareit.geteway.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.geteway.user.dto.UserDto;
import shareit.geteway.validation.CreateValidationObject;
import shareit.geteway.validation.UpdateValidationObject;

import javax.validation.constraints.Min;
import java.net.URISyntaxException;
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

    UserClient client;
    private static final String URI_ID_USER = "/{userId}";

    @PostMapping
    public ResponseEntity<?> postRequestUser(@RequestBody @Validated(CreateValidationObject.class) UserDto dto) throws URISyntaxException {
        log.info("Вход в geteway");
        return client.addUser(dto);
    }

    @PatchMapping(path = URI_ID_USER)
    public ResponseEntity<?> updateUser(@PathVariable @Min(1) Long userId,
                                        @RequestBody @Validated(UpdateValidationObject.class) UserDto dto) {
        return client.updateUser(userId, dto);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return client.getAllUsers();
    }

    @GetMapping(path = URI_ID_USER)
    public ResponseEntity<?> getUserById(@PathVariable @Min(1) Long userId) {
        return client.getUserById(userId);
    }

    @DeleteMapping(path = URI_ID_USER)
    public void deleteUser(@PathVariable @Min(1) Long userId) {
        client.deleteUser(userId);
    }
}
