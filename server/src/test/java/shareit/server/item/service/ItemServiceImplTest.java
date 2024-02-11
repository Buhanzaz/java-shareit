package shareit.server.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import shareit.server.booking.enums.Status;
import shareit.server.booking.model.Booking;
import shareit.server.booking.repository.BookingRepository;
import shareit.server.exception.BookingException;
import shareit.server.exception.NotFoundException;
import shareit.server.item.dto.CommentDto;
import shareit.server.item.dto.ItemDto;
import shareit.server.item.model.Item;
import shareit.server.request.dto.ItemRequestDto;
import shareit.server.request.service.ItemRequestService;
import shareit.server.user.model.User;
import shareit.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Sql(scripts = {"file:src/main/resources/schema.sql"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemServiceImplTest {
    final ItemService itemService;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;
    final ItemRequestService itemRequestService;

    ItemDto itemDto;
    ItemDto updateItemDto;
    CommentDto commentDto;
    User firstUser;
    User secondUser;
    Booking lastBooking;
    Booking nextBooking;
    ItemRequestDto onlyDescriptionRequestDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
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

        firstUser = User.builder()
                .id(1L)
                .name("Test Name")
                .email("Test@mail.com")
                .build();

        secondUser = User.builder()
                .id(1L)
                .name("Test Name 2")
                .email("Test2@mail.com")
                .build();

        onlyDescriptionRequestDto = ItemRequestDto.builder()
                .description("Test Description")
                .build();
    }

    @Test
    void addAndGetByIdItem() {
        firstUser = userRepository.save(firstUser);

        ItemRequestDto savedRequest = itemRequestService.addItemRequest(firstUser.getId(), onlyDescriptionRequestDto);

        itemDto.setRequestId(savedRequest.getId());

        ItemDto createItem = itemService.addItem(firstUser.getId(), itemDto);
        ItemDto getItem = itemService.getItemById(firstUser.getId(), createItem.getId());

        assertThat(createItem).usingRecursiveComparison().ignoringFields("comments").isEqualTo(getItem);
    }

    @Test
    void addItemNotFoundExceptionUser() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.addItem(firstUser.getId(), itemDto));

        assertEquals("Юзер с id " + firstUser.getId() + " не найден", exception.getMessage());
    }

    @Test
    void addItemNotFoundExceptionItemRequest() {
        userRepository.save(firstUser);
        itemDto.setRequestId(1L);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.addItem(firstUser.getId(), itemDto));

        assertEquals(String.format("Запроса на создание вещи с id %d не найдено",
                itemDto.getRequestId()), exception.getMessage());
    }

    @Test
    void updateItem() {
        User savedUser = userRepository.save(firstUser);

        ItemDto createItem = itemService.addItem(savedUser.getId(), itemDto);
        ItemDto updatedItem = itemService.updateItem(createItem.getId(), savedUser.getId(), updateItemDto);

        assertEquals(updatedItem.getId(), updateItemDto.getId());
        assertEquals(updatedItem.getName(), updateItemDto.getName());
        assertEquals(updatedItem.getDescription(), updateItemDto.getDescription());
        assertEquals(updatedItem.getAvailable(), updateItemDto.getAvailable());
    }

    @Test
    void getAllItemsOwner() {
        User savedUser1 = userRepository.save(firstUser);
        User savedUser2 = userRepository.save(secondUser);
        ItemDto createItem1 = itemService.addItem(savedUser1.getId(), itemDto);
        ItemDto createItem2 = itemService.addItem(savedUser2.getId(), itemDto);

        createLastAndNextBookings(createItem1, savedUser1, savedUser2);
        createLastAndNextBookings(createItem2, savedUser1, savedUser2);
        bookingRepository.save(lastBooking);
        bookingRepository.save(nextBooking);

        ItemDto findItem = itemService.getItemById(savedUser1.getId(), createItem1.getId());

        List<ItemDto> personalItemsList = itemService.getAllItemsOwner(savedUser1.getId());

        assertThat(personalItemsList.get(0)).usingRecursiveComparison()
                .ignoringFields("comments").isEqualTo(findItem);
    }

    @Test
    void itemSearch() {
        User savedUser = userRepository.save(firstUser);
        ItemDto createItem1 = itemService.addItem(savedUser.getId(), itemDto);
        ItemDto createItem2 = itemService.addItem(savedUser.getId(), itemDto);

        List<ItemDto> findItems = itemService.itemSearch("test", savedUser.getId(), 0, 2);

        assertThat(findItems.size()).isEqualTo(2);
        assertThat(findItems.get(0)).usingRecursiveComparison()
                .ignoringFields("comments").isEqualTo(createItem1);
        assertThat(findItems.get(1)).usingRecursiveComparison()
                .ignoringFields("comments").isEqualTo(createItem2);
    }

    @Test
    void itemSearchEmptyList() {
        List<ItemDto> findItems = itemService.itemSearch("", firstUser.getId(), 0, 2);

        assertThat(findItems.size()).isEqualTo(0);
    }

    @Test
    void addComment() {
        User savedUser = userRepository.save(firstUser);
        ItemDto createItem = itemService.addItem(savedUser.getId(), itemDto);

        createLastAndNextBookings(createItem, firstUser, secondUser);
        bookingRepository.save(lastBooking);
        itemService.addComment(savedUser.getId(), createItem.getId(), commentDto);

        ItemDto itemById = itemService.getItemById(createItem.getId(), savedUser.getId());

        assertEquals(itemById.getComments().get(0).getText(), commentDto.getText());
    }

    @Test
    void addCommentBookingException() {
        User savedUser = userRepository.save(firstUser);
        ItemDto createItem = itemService.addItem(savedUser.getId(), itemDto);
        BookingException exception = assertThrows(BookingException.class,
                () -> itemService.addComment(savedUser.getId(), createItem.getId(), commentDto));

        assertEquals("Вы не можете ставить комментарии под вещью которую не бронировали ранее.", exception.getMessage());
    }

    private void createLastAndNextBookings(ItemDto item, User firstUser, User secondUser) {
        Item bookingItem = Item.builder()
                .id(item.getId())
                .user(firstUser)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable()).build();

        lastBooking = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(bookingItem)
                .booker(secondUser)
                .status(Status.APPROVED).build();

        nextBooking = Booking.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .item(bookingItem)
                .booker(secondUser)
                .status(Status.APPROVED).build();
    }
}