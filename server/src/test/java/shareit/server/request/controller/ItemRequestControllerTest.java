package shareit.server.request.controller;

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
import shareit.server.request.dto.ItemRequestDto;
import shareit.server.request.service.ItemRequestServiceImpl;
import shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemRequestControllerTest {
    @MockBean
    final ItemRequestServiceImpl itemRequestService;
    final MockMvc mockMvc;
    final ObjectMapper objectMapper;
    final String headerIdUser = "X-Sharer-User-Id";
    ItemRequestDto onlyDescriptionRequestDto;
    ItemRequestDto fullRequestDto;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Test Name")
                .email("Test@email.com")
                .build();

        onlyDescriptionRequestDto = ItemRequestDto.builder()
                .description("Test Description")
                .build();

        fullRequestDto = ItemRequestDto.builder()
                .id(1L)
                .creator(userDto)
                .description("Test Description")
                .created(LocalDateTime.now().plusMinutes(1))
                .items(null)
                .build();
    }

    @Test
    @SneakyThrows
    void postRequestAddItemRequest() {
        when(itemRequestService.addItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(fullRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .header(headerIdUser, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(onlyDescriptionRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ItemRequestDto returnItemRequestDto = objectMapper.readValue(result, ItemRequestDto.class);

        assertEquals(returnItemRequestDto, fullRequestDto);
    }

    @Test
    @SneakyThrows
    void getMappingSearchAllItemsRequestsCreator() {
        List<ItemRequestDto> itemRequestDtoList = List.of(fullRequestDto);
        when(itemRequestService.searchAllItemsRequestsCreator(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtoList);

        String result = mockMvc.perform(get("/requests")
                        .header(headerIdUser, 1)
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getMappingSearchAllItemsRequests() {
        List<ItemRequestDto> itemRequestDtoList = List.of(fullRequestDto);
        when(itemRequestService.searchAllItemsRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtoList);

        String result = mockMvc.perform(get("/requests/all")
                        .header(headerIdUser, 1)
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDtoList), result);
    }

    @Test
    @SneakyThrows
    void getMappingSearchItemsRequestsById() {
        when(itemRequestService.searchAllItemsRequestsById(anyLong(), anyLong()))
                .thenReturn(fullRequestDto);

        String result = mockMvc.perform(get("/requests/{requestId}", fullRequestDto.getId())
                        .header(headerIdUser, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ItemRequestDto returnItemRequestDto = objectMapper.readValue(result, ItemRequestDto.class);

        assertEquals(returnItemRequestDto, fullRequestDto);
    }
}