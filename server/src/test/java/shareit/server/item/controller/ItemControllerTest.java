package shareit.server.item.controller;

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
import shareit.server.item.dto.CommentDto;
import shareit.server.item.dto.ItemDto;
import shareit.server.item.model.Item;
import shareit.server.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemControllerTest {
    @MockBean
    final ItemService itemService;
    final MockMvc mockMvc;
    final ObjectMapper objectMapper;
    final String headerIdUser = "X-Sharer-User-Id";

    Item item;
    ItemDto itemDto;
    ItemDto updateItemDto;
    CommentDto commentDto;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .name("Test Name")
                .description("Test Description")
                .available(true)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Name")
                .description("Test Description")
                .available(true)
                .build();

        updateItemDto = ItemDto.builder()
                .id(1L)
                .name("Test Name Update")
                .description("Test Description Update")
                .available(true)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .authorName("Test Name")
                .text("Test Text")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    @SneakyThrows
    void postRequestItem_isOk() {
        when(itemService.addItem(anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header(headerIdUser, 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ItemDto returnItemDto = objectMapper.readValue(result, ItemDto.class);

        assertEquals(returnItemDto, itemDto);
    }

    @Test
    @SneakyThrows
    void patchRequestItem_isOk() {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class)))
                .thenReturn(updateItemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", item.getId())
                        .header(headerIdUser, 1)
                        .content(objectMapper.writeValueAsString(updateItemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateItemDto.getId()))
                .andExpect(jsonPath("$.name").value(updateItemDto.getName()))
                .andExpect(jsonPath("$.description").value(updateItemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(updateItemDto.getAvailable()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ItemDto returnUserDto = objectMapper.readValue(result, ItemDto.class);

        assertNotEquals(returnUserDto, itemDto);
        assertEquals(returnUserDto, updateItemDto);
    }

    @Test
    @SneakyThrows
    void getRequestSearchItemById_isOk() {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDto);

        String result = mockMvc.perform(get("/items/{itemId}", itemDto.getId())
                        .header(headerIdUser, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ItemDto returnUserDto = objectMapper.readValue(result, ItemDto.class);

        assertEquals(itemDto, returnUserDto);
    }

    @Test
    @SneakyThrows
    void getRequestItemsOwner_isOk() {
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemService.getAllItemsOwner(anyLong()))
                .thenReturn(itemDtoList);

        String result = mockMvc.perform(get("/items", itemDto.getId())
                        .header(headerIdUser, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @Test
    @SneakyThrows
    void getRequestItemSearch_isOk() {
        List<ItemDto> itemDtoList = List.of(itemDto);

        when(itemService.itemSearch(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(itemDtoList);

        String result = mockMvc.perform(get("/items/search")
                        .param("from", "0")
                        .param("size", "1")
                        .param("text", "test")
                        .header(headerIdUser, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @Test
    @SneakyThrows
    void postRequestAddComment_isOk() {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/1/comment")
                        .header(headerIdUser, 1)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CommentDto returnComment = objectMapper.readValue(result, CommentDto.class);

        assertEquals(returnComment, commentDto);
    }
}