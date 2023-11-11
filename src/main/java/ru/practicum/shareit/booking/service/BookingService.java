package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.enums.State;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingDto addNewBooking(Long userId, ClientRequestBookingDto dto);

    BookingDto ownerResponseToTheBooking(Long userId, Boolean approved, Long bookingId);

    BookingDto findBookingForAuthorOrOwner(Long userId, Long bookingId) throws RuntimeException;

    List<BookingDto> findAllBookingsForBooker(Long userId, String state, Integer from, Integer size);

    List<BookingDto> findAllBookingsForOwner(Long userId, String state, Integer from, Integer size);
}
