package shareit.server.booking.controller;

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
import shareit.server.booking.dto.BookingDto;
import shareit.server.booking.dto.ClientRequestBookingDto;
import shareit.server.booking.enums.Status;
import shareit.server.booking.service.BookingService;
import shareit.server.exception.ValidateException;
import shareit.server.item.dto.ItemDto;
import shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class BookingControllerTest {
    @MockBean
    final BookingService bookingService;
    final MockMvc mockMvc;
    final ObjectMapper objectMapper;
    final String headerIdUser = "X-Sharer-User-Id";

    ItemDto itemDto;
    UserDto userDto;
    BookingDto bookingDto;
    BookingDto bookingDtoRejected;
    ClientRequestBookingDto clientRequestBookingDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("Test Name")
                .email("Test@mail.com")
                .build();

        clientRequestBookingDto = ClientRequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(itemDto)
                .booker(userDto)
                .status(Status.APPROVED)
                .build();

        bookingDtoRejected = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(itemDto)
                .booker(userDto)
                .status(Status.REJECTED)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Name")
                .description("Test Description")
                .available(true)
                .build();
    }

    @Test
    @SneakyThrows
    void addNewBooking() {
        when(bookingService.addNewBooking(anyLong(), any(ClientRequestBookingDto.class)))
                .thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header(headerIdUser, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestBookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookingDto dto = objectMapper.readValue(result, BookingDto.class);

        assertEquals(dto, bookingDto);
    }

    @Test
    @SneakyThrows
    void addNewBookingValidateException() {
        when(bookingService.addNewBooking(anyLong(), any(ClientRequestBookingDto.class)))
                .thenThrow(ValidateException.class);

        mockMvc.perform(post("/bookings")
                        .param("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(clientRequestBookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    @SneakyThrows
    void ownerResponseToTheBooking() {
        when(bookingService.ownerResponseToTheBooking(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingDtoRejected);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                        .param("approved", "true")
                        .header(headerIdUser, userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookingDto dto = objectMapper.readValue(result, BookingDto.class);

        assertEquals(dto, bookingDtoRejected);
    }

    @Test
    @SneakyThrows
    void findBookingForAuthorOrOwner() {
        when(bookingService.findBookingForAuthorOrOwner(anyLong(), anyLong()))
                .thenReturn(bookingDto);


        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(headerIdUser, userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);

    }

    @Test
    @SneakyThrows
    void findAllBookingsForUser() {
        when(bookingService.findAllBookingsForBooker(any(), any(), any(), any()))
                .thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings")
                        .header(headerIdUser, userDto.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @Test
    @SneakyThrows
    void getAllBookingsForOwner() {
        when(bookingService.findAllBookingsForOwner(any(), any(), any(), any()))
                .thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings/owner")
                        .header(headerIdUser, userDto.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }
}