package shareit.geteway.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.geteway.user.client.UserClient;
import shareit.geteway.user.dto.UserDto;
import shareit.geteway.validation.CreateValidationObject;
import shareit.geteway.validation.UpdateValidationObject;

import javax.validation.constraints.Min;
import java.net.URISyntaxException;

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

    UserClient webClient;
    private static final String URI_ID_USER = "/{userId}";

    @PostMapping
    public ResponseEntity<?> postRequestUser(@RequestBody @Validated(CreateValidationObject.class) UserDto dto) throws URISyntaxException {
        log.info("Вход в geteway");
        return webClient.addUser(dto);
    }

    @PatchMapping(path = URI_ID_USER)
    public ResponseEntity<?> updateUser(@PathVariable @Min(1) Long userId,
                                        @RequestBody @Validated(UpdateValidationObject.class) UserDto dto) {
        return webClient.updateUser(userId, dto);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return webClient.getAllUsers();
    }

    @GetMapping(path = URI_ID_USER)
    public ResponseEntity<?> getUserById(@PathVariable @Min(1) Long userId) {
        return webClient.getUserById(userId);
    }

    @DeleteMapping(path = URI_ID_USER)
    public void deleteUser(@PathVariable @Min(1) Long userId) {
        webClient.deleteUser(userId);
    }
}
