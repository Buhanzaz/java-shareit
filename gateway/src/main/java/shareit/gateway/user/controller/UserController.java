package shareit.gateway.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.gateway.user.client.UserClient;
import shareit.gateway.user.dto.UserDto;
import shareit.gateway.validation.interfaces.CreateValidationObject;
import shareit.gateway.validation.interfaces.UpdateValidationObject;

import javax.validation.constraints.Min;

@Slf4j
@Validated
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient webClient;
    private static final String URI_ID_USER = "/{userId}";

    @PostMapping
    public ResponseEntity<?> postRequestUser(@RequestBody @Validated(CreateValidationObject.class) UserDto dto) {
        log.info("Вход в gateway");
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

    @ResponseBody
    @DeleteMapping(path = URI_ID_USER)
    public void deleteUser(@PathVariable @Min(1) Long userId) {
        webClient.deleteUser(userId);
    }
}
