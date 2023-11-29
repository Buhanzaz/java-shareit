package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Sql(scripts = {"file:src/main/resources/schema.sql"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceImplTest {
    final UserRepository userRepository;
    final ItemRepository itemRepository;
    final BookingServiceImpl bookingService;
    final BookingRepository bookingRepository;

    User firstUser;
    User secondUser;
    Item firstItem;
    Item secondItem;
    ClientRequestBookingDto clientRequestBookingDto;

    @BeforeEach
    void setUp() {
        firstUser = User.builder()
                .name("Test Name")
                .email("Test@email.ru")
                .build();

        secondUser = User.builder()
                .name("Test Name 2")
                .email("Test2@email.ru")
                .build();

        firstItem = Item.builder()
                .name("Test Name")
                .description("Test Description")
                .available(Boolean.TRUE)
                .user(secondUser)
                .itemRequest(null).build();

        secondItem = Item.builder()
                .name("Test Name")
                .description("Test Description")
                .available(Boolean.TRUE)
                .user(secondUser)
                .itemRequest(null).build();

        clientRequestBookingDto = ClientRequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    void addNewBooking() {
        User savedUser = userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);

        BookingDto savedBooking = bookingService.addNewBooking(savedUser.getId(), clientRequestBookingDto);
        BookingDto findBooking = bookingService
                .findBookingForAuthorOrOwner(secondUser.getId(), savedBooking.getId());

        assertThat(savedBooking).usingRecursiveComparison().ignoringFields("start", "end")
                .isEqualTo(findBooking);

    }

    @Test
    void addNewBookingValidateException() {
        User savedUser = userRepository.save(firstUser);
        userRepository.save(secondUser);
        firstItem.setAvailable(false);
        itemRepository.save(firstItem);

        ValidateException exception = assertThrows(ValidateException.class,
                () -> bookingService.addNewBooking(savedUser.getId(), clientRequestBookingDto));

        assertEquals("Вещь уже забронирована", exception.getMessage());
    }

    @Test
    void addNewBookingDataTimeException() {
        User savedUser = userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);

        clientRequestBookingDto.setEnd(LocalDateTime.now().minusDays(3));

        DataTimeException exception = assertThrows(DataTimeException.class,
                () -> bookingService.addNewBooking(savedUser.getId(), clientRequestBookingDto));

        assertEquals("Ошибка! Начало бронирования не может быть позже конца бронирования!", exception.getMessage());
    }

    @Test
    void addNewBookingNotFoundExceptionUser() {
        firstUser.setId(1L);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.addNewBooking(firstUser.getId(), clientRequestBookingDto));

        assertEquals("Вы не зарегистрированы!", exception.getMessage());
    }

    @Test
    void ownerResponseToTheBooking_true() {
        User savedUser = userRepository.save(firstUser);

        userRepository.save(secondUser);
        itemRepository.save(firstItem);

        BookingDto savedBooking = bookingService.addNewBooking(savedUser.getId(), clientRequestBookingDto);

        BookingDto approvedBooking = bookingService
                .ownerResponseToTheBooking(secondUser.getId(), true, savedBooking.getId());
        BookingDto findBooking = bookingService
                .findBookingForAuthorOrOwner(secondUser.getId(), savedBooking.getId());

        assertThat(approvedBooking).usingRecursiveComparison().isEqualTo(findBooking);
    }

    @Test
    void ownerResponseToTheBooking_false() {
        User savedUser = userRepository.save(firstUser);

        userRepository.save(secondUser);
        itemRepository.save(firstItem);

        BookingDto savedBooking = bookingService.addNewBooking(savedUser.getId(), clientRequestBookingDto);

        BookingDto approvedBooking = bookingService
                .ownerResponseToTheBooking(secondUser.getId(), false, savedBooking.getId());
        BookingDto findBooking = bookingService
                .findBookingForAuthorOrOwner(secondUser.getId(), savedBooking.getId());

        assertThat(approvedBooking).usingRecursiveComparison().isEqualTo(findBooking);
    }

    @Test
    void ownerResponseToTheBookingException() {
        firstUser = userRepository.save(firstUser);

        userRepository.save(secondUser);
        itemRepository.save(firstItem);

        BookingDto savedBooking = bookingService.addNewBooking(firstUser.getId(), clientRequestBookingDto);

        bookingService
                .ownerResponseToTheBooking(secondUser.getId(), true, savedBooking.getId());

        BookingException exception = assertThrows(BookingException.class,
                () -> bookingService
                        .ownerResponseToTheBooking(secondUser.getId(), true, savedBooking.getId()));

        assertEquals("Вы уже подтвердили бронирование вашей вещи.", exception.getMessage());
    }

    @Test
    void ownerResponseToTheNotFoundExceptionUser() {
        firstUser = userRepository.save(firstUser);

        userRepository.save(secondUser);
        itemRepository.save(firstItem);

        BookingDto savedBooking = bookingService.addNewBooking(firstUser.getId(), clientRequestBookingDto);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService
                        .ownerResponseToTheBooking(3L, true, savedBooking.getId()));

        assertEquals("Вы не зарегистрированы", exception.getMessage());
    }

    @Test
    void findBookingForAuthorOrOwner() {
        User savedUser = userRepository.save(firstUser);

        userRepository.save(secondUser);
        itemRepository.save(firstItem);

        BookingDto savedBooking = bookingService.addNewBooking(savedUser.getId(), clientRequestBookingDto);

        BookingDto approvedBooking = bookingService
                .findBookingForAuthorOrOwner(savedUser.getId(), savedBooking.getId());
        BookingDto findBooking = bookingService
                .findBookingForAuthorOrOwner(secondUser.getId(), savedBooking.getId());

        assertThat(approvedBooking).usingRecursiveComparison().isEqualTo(findBooking);
    }

    @Test
    void findBookingForAuthorOrOwnerNotFoundExceptionUser() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService
                        .findBookingForAuthorOrOwner(1L, 1L));

        assertEquals("Вы не зарегистрированы", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForBooker_all_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking currentBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking currentBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(currentBookingForItem1);
        bookingRepository.save(currentBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForBooker(secondUser.getId(), "ALL", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), currentBookingForItem2.getId());
        assertEquals(bookingId.get(1), currentBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForBooker_all_isEnumError() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking currentBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking currentBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(currentBookingForItem1);
        bookingRepository.save(currentBookingForItem2);

        EnumException exception = assertThrows(EnumException.class,
                () -> bookingService
                        .findAllBookingsForBooker(secondUser.getId(), "TEST", 0, 10));

        assertEquals("Unknown state: TEST", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForBooker_current_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking currentBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking currentBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(currentBookingForItem1);
        bookingRepository.save(currentBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForBooker(secondUser.getId(), "CURRENT", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), currentBookingForItem2.getId());
        assertEquals(bookingId.get(1), currentBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForBooker_past_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking pastBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking pastBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();


        bookingRepository.save(pastBookingForItem1);
        bookingRepository.save(pastBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForBooker(secondUser.getId(), "PAST", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), pastBookingForItem2.getId());
        assertEquals(bookingId.get(1), pastBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForBooker_future_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking futureBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking futureBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();


        bookingRepository.save(futureBookingForItem1);
        bookingRepository.save(futureBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForBooker(secondUser.getId(), "FUTURE", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), futureBookingForItem2.getId());
        assertEquals(bookingId.get(1), futureBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForBooker_waiting_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking waitingBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.WAITING)
                .build();

        Thread.sleep(25);

        Booking waitingBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.WAITING)
                .build();


        bookingRepository.save(waitingBookingForItem1);
        bookingRepository.save(waitingBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForBooker(secondUser.getId(), "WAITING", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), waitingBookingForItem2.getId());
        assertEquals(bookingId.get(1), waitingBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForBooker_rejected_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking rejectedBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.REJECTED)
                .build();

        Thread.sleep(25);

        Booking rejectedBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.REJECTED)
                .build();


        bookingRepository.save(rejectedBookingForItem1);
        bookingRepository.save(rejectedBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForBooker(secondUser.getId(), "REJECTED", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), rejectedBookingForItem2.getId());
        assertEquals(bookingId.get(1), rejectedBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForOwner_all_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking currentBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking currentBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(currentBookingForItem1);
        bookingRepository.save(currentBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForOwner(secondUser.getId(), "ALL", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), currentBookingForItem2.getId());
        assertEquals(bookingId.get(1), currentBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForOwner_current_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking currentBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking currentBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        bookingRepository.save(currentBookingForItem1);
        bookingRepository.save(currentBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForOwner(secondUser.getId(), "CURRENT", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), currentBookingForItem2.getId());
        assertEquals(bookingId.get(1), currentBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForOwner_past_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking pastBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking pastBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();


        bookingRepository.save(pastBookingForItem1);
        bookingRepository.save(pastBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForOwner(secondUser.getId(), "PAST", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), pastBookingForItem2.getId());
        assertEquals(bookingId.get(1), pastBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForOwner_future_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking futureBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();

        Thread.sleep(25);

        Booking futureBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.APPROVED)
                .build();


        bookingRepository.save(futureBookingForItem1);
        bookingRepository.save(futureBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForOwner(secondUser.getId(), "FUTURE", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), futureBookingForItem2.getId());
        assertEquals(bookingId.get(1), futureBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForOwner_waiting_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking waitingBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.WAITING)
                .build();

        Thread.sleep(25);

        Booking waitingBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.WAITING)
                .build();


        bookingRepository.save(waitingBookingForItem1);
        bookingRepository.save(waitingBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForOwner(secondUser.getId(), "WAITING", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), waitingBookingForItem2.getId());
        assertEquals(bookingId.get(1), waitingBookingForItem1.getId());
    }

    @Test
    @SneakyThrows
    void findAllBookingsForOwner_rejected_isOk() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        itemRepository.save(firstItem);
        itemRepository.save(secondItem);

        Booking rejectedBookingForItem1 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(firstItem)
                .booker(secondUser)
                .status(Status.REJECTED)
                .build();

        Thread.sleep(25);

        Booking rejectedBookingForItem2 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(secondItem)
                .booker(secondUser)
                .status(Status.REJECTED)
                .build();


        bookingRepository.save(rejectedBookingForItem1);
        bookingRepository.save(rejectedBookingForItem2);

        List<BookingDto> findBookingList = bookingService
                .findAllBookingsForOwner(secondUser.getId(), "REJECTED", 0, 10);

        assertThat(findBookingList.size()).isEqualTo(2);

        List<Long> bookingId = findBookingList.stream().map(BookingDto::getId).collect(Collectors.toList());

        assertEquals(bookingId.get(0), rejectedBookingForItem2.getId());
        assertEquals(bookingId.get(1), rejectedBookingForItem1.getId());
    }
}