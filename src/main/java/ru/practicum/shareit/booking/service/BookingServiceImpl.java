package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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

import static ru.practicum.shareit.utils.Pages.getPageForBooking;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@RequiredArgsConstructor
class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;
    BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto addNewBooking(Long userId, ClientRequestBookingDto dto) {
        validationTimeFromDto(dto);

        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Вы не зарегистрированы!"));
        Item item = itemRepository.findByIdFetchEgle(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещи с таким id не найдено"));
        Booking booking = bookingMapper.clientRequestDtoToModel(dto, booker, item, Status.WAITING);

        if (!booking.getItem().getAvailable())
            throw new ValidateException("Вещь уже забронирована");
        if (userId.equals(booking.getItem().getUser().getId()))
            throw new NotFoundException("Нельзя бронировать вещь у самого себя");

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto ownerResponseToTheBooking(Long userId, Boolean approved, Long bookingId) {
        Booking booking;

        if (userRepository.existsById(userId)) {
            booking = bookingRepository
                    .findBookingByIdAndItem_User_Id(bookingId, userId)
                    .orElseThrow(() -> new NotFoundException(String.format("Бронирование с данным %d не найдено", bookingId)));
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new BookingException("Вы уже подтвердили бронирование вашей вещи.");
            }

            booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        } else {
            throw new NotFoundException("Вы не зарегистрированы");
        }

        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto findBookingForAuthorOrOwner(Long userId, Long bookingId) {
        Booking booking;

        if (userRepository.existsById(userId)) {
            booking = bookingRepository
                    .findBookingForAuthorBookingOrOwnerItem(userId, bookingId)
                    .orElseThrow(() -> new NotFoundException(String.format("Бронирование с данным %d не найдено", bookingId)));
        } else {
            throw new NotFoundException("Вы не зарегистрированы");
        }

        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAllBookingsForBooker(Long userId, String state, Integer from, Integer size) {
        State stateEnum;

        if (userRepository.existsById(userId)) {
            try {
                stateEnum = State.valueOf(state);
            } catch (IllegalArgumentException e) {
                throw new EnumException(String.format("Unknown state: %s", state));
            }

            LocalDateTime dateTimeNow = LocalDateTime.now();

            switch (stateEnum) {
                case ALL:
                    return bookingRepository.findByBooker_Id(userId, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, dateTimeNow, dateTimeNow, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findByBookerIdAndEndIsBefore(userId, dateTimeNow, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findByBookerIdAndStartIsAfter(userId, dateTimeNow, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepository.findByBookerIdAndStartIsAfterAndStatusIs(userId, dateTimeNow, Status.WAITING, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case REJECTED:
                    return bookingRepository.findByBookerIdAndStatusIs(userId, Status.REJECTED, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
            }
        }
        throw new NotFoundException("Вы не зарегистрированы");
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findAllBookingsForOwner(Long userId, String state, Integer from, Integer size) {
        State stateEnum;

        if (userRepository.existsById(userId)) {
            try {
                stateEnum = State.valueOf(state);
            } catch (IllegalArgumentException e) {
                throw new EnumException(String.format("Unknown state: %s", state));
            }

            LocalDateTime dateTimeNow = LocalDateTime.now();

            switch (stateEnum) {
                case ALL:
                    return bookingRepository.findByItem_User_Id(userId, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepository.findByItem_User_IdAndStartIsBeforeAndEndIsAfter(userId, dateTimeNow, dateTimeNow, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case PAST:
                    return bookingRepository.findByItem_User_IdAndEndIsBefore(userId, dateTimeNow, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepository.findByItem_User_IdAndStartIsAfter(userId, dateTimeNow, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepository.findByItem_User_IdAndStartIsAfterAndStatusIs(userId, dateTimeNow, Status.WAITING, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case REJECTED:
                    return bookingRepository.findByItem_User_IdAndStatusIs(userId, Status.REJECTED, getPageForBooking(from, size))
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
            }
        }
        throw new NotFoundException("Вы не зарегистрированы");
    }

    private void validationTimeFromDto(ClientRequestBookingDto dto) {
        if (dto.getEnd().isBefore(dto.getStart()) || dto.getStart().equals(dto.getEnd()))
            throw new DataTimeException("Ошибка! Начало бронирования не может быть позже конца бронирования!");
    }
}
