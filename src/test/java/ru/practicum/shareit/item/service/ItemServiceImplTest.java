package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Sql(scripts = {"file:src/main/resources/schema.sql"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemServiceImplTest {
    final ItemServiceImpl itemService;
    final UserRepository userRepository;
    final BookingRepository bookingRepository;

    ItemDto itemDto;
    ItemDto updateItemDto;
    CommentDto commentDto;
    User firstUser;
    User secondUser;
    Booking lastBooking;
    Booking nextBooking;


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
    }

    @Test
    void addAndGetByIdItem() {
        User savedUser = userRepository.save(firstUser);

        ItemDto createItem = itemService.addItem(savedUser.getId(), itemDto);
        ItemDto getItem = itemService.getItemById(savedUser.getId(), createItem.getId());

        assertThat(createItem).usingRecursiveComparison().ignoringFields("comments").isEqualTo(getItem);
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
    void addComment() {
        User savedUser = userRepository.save(firstUser);
        ItemDto createItem = itemService.addItem(savedUser.getId(), itemDto);

        createLastAndNextBookings(createItem, firstUser, secondUser);
        bookingRepository.save(lastBooking);
        itemService.addComment(savedUser.getId(), createItem.getId(), commentDto);

        ItemDto itemById = itemService.getItemById(createItem.getId(), savedUser.getId());

        assertEquals(itemById.getComments().get(0).getText(), commentDto.getText());
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