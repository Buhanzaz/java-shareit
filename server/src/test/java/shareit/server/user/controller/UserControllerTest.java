package shareit.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shareit.server.exception.NotFoundException;
import shareit.server.user.dto.UserDto;
import shareit.server.user.model.User;
import shareit.server.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserControllerTest {
    final ObjectMapper objectMapper;
    final MockMvc mockMvc;

    @MockBean
    final UserService userService;

    User user;
    UserDto userDto;
    UserDto updateUserDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Test Name")
                .email("Test@mail.com")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("Test Name")
                .email("Test@mail.com")
                .build();

        updateUserDto = UserDto.builder()
                .id(1L)
                .name("Test Name Update")
                .email("TestUpdate@mail.com")
                .build();
    }

    @Test
    @SneakyThrows
    void getRequestAllUsers_isOK() {
        List<UserDto> userDtoList = List.of(userDto);
        when(userService.getAllUsers())
                .thenReturn(userDtoList);

        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDtoList), result);
    }

    @Test
    @SneakyThrows
    void postRequestUser_isOK() {
        when(userService.addUser(any(UserDto.class)))
                .thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto returnUserDto = objectMapper.readValue(result, UserDto.class);

        assertEquals(returnUserDto, userDto);
    }

    @Test
    @SneakyThrows
    void deleteRequestUser_isOK() {
        mockMvc.perform(delete("/users/{userId}", user.getId()))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(anyLong());
    }

    @Test
    @SneakyThrows
    void patchRequestUser_isOK() {
        when(userService.updateUser(anyLong(), any(UserDto.class)))
                .thenReturn(updateUserDto);

        String result = mockMvc.perform(patch("/users/{userId}", user.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateUserDto.getId()))
                .andExpect(jsonPath("$.name").value(updateUserDto.getName()))
                .andExpect(jsonPath("$.email").value(updateUserDto.getEmail()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto returnUserDto = objectMapper.readValue(result, UserDto.class);

        assertNotEquals(returnUserDto, userDto);
        assertEquals(returnUserDto, updateUserDto);
    }

    @Test
    @SneakyThrows
    void getRequestUser_isOK() {
        when(userService.getUserById(anyLong()))
                .thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto returnUserDto = objectMapper.readValue(result, UserDto.class);

        assertEquals(objectMapper.writeValueAsString(returnUserDto), result);
    }

    @Test
    @SneakyThrows
    public void updateUserNotFound() {
        when(userService.updateUser(anyLong(), any(UserDto.class)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/users/{userId}", user.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                );
    }
}