package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemRepositoryInDB;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepositoryInDB;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class BookingServiceImplInDB implements BookingService {

    BookingRepository bookingRepository;
    UserRepositoryInDB userRepository;
    ItemRepositoryInDB itemRepository;
    BookingMapper bookingMapper;


    @Override
    public BookingDto addNewBooking(Long userId, ClientRequestBookingDto dto) {
        User booker = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Вы не зарегистрированы!"));
        Item item = itemRepository.findByIdFetchEgle(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещи с таким id не найдено"));

        validationTimeFromDto(dto);

        Booking booking = bookingMapper.clientRequestToModel(dto, booker, item, Status.WAITING);

        validationBooking(userId, booking);



//        booking.setItem(item);
//        booking.setStatus(Status.WAITING);
//        booking.setBooker(booker);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
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

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
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
    public List<BookingDto> findAllBookingsForBooker(Long userId, String state) {
        //TODO Доделать представление в виде страниц
        if (bookingRepository.existsById(userId)) {
            LocalDateTime dateTimeNow = LocalDateTime.now();

            switch (state) {
                case "ALL":
                    return bookingRepository.findByBookerIdOrderByStartDesc(userId)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "CURRENT":
                    return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, dateTimeNow, dateTimeNow)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "PAST":
                    return bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, dateTimeNow)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "FUTURE":
                    return bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, dateTimeNow)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "WAITING":
                    return bookingRepository.findByBookerIdAndStartIsAfterAndStatusIsOrderByStartDesc(userId, dateTimeNow, Status.WAITING)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "REJECTED":
                    return bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(userId, Status.REJECTED)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                default:
                    throw new BadRequestException(String.format("Unknown state: %s", state));
            }
        }
        throw new NotFoundException("Вы не зарегистрированы");
    }

    @Override
    public List<BookingDto> findAllBookingsForOwner(Long userId, String state) {
        //TODO Доделать представление в виде страниц
        if (userRepository.existsById(userId)) {
            LocalDateTime dateTimeNow = LocalDateTime.now();

            switch (state) {
                case "ALL":
                    return bookingRepository.findByItem_User_IdOrderByStartDesc(userId)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "CURRENT":
                    return bookingRepository.findByItem_User_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, dateTimeNow, dateTimeNow)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "PAST":
                    return bookingRepository.findByItem_User_IdAndEndIsBeforeOrderByStartDesc(userId, dateTimeNow)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "FUTURE":
                    return bookingRepository.findByItem_User_IdAndStartIsAfterOrderByStartDesc(userId, dateTimeNow)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "WAITING":
                    return bookingRepository.findByItem_User_IdAndStartIsAfterAndStatusIsOrderByStartDesc(userId, dateTimeNow, Status.WAITING)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                case "REJECTED":
                    return bookingRepository.findByItem_User_IdAndStatusIsOrderByStartDesc(userId, Status.REJECTED)
                            .stream()
                            .map(bookingMapper::toDto)
                            .collect(Collectors.toList());
                default:
                    throw new BadRequestException(String.format("Unknown state: %s", state));
            }
        } else {
            throw new NotFoundException("Вы не зарегистрированы");
        }
    }

    private void validationTimeFromDto(ClientRequestBookingDto dto) {
        if (dto.getEnd().isBefore(dto.getStart()) || dto.getStart().equals(dto.getEnd()))
            throw new DataTimeException("Ошибка! Начало бронирования не может быть позже конца бронирования!");
    }

    private void validationBooking(Long userId, Booking booking) {
        if (!booking.getItem().getAvailable())
            throw new ValidateException("Вещь уже забронирована");
        if (userId.equals(booking.getItem().getUser().getId()))
            throw new NotFoundException("Нельзя бронировать вещь у самого себя");
    }
}
