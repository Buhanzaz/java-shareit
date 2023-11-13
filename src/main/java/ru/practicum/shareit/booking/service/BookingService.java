package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(Long userId, ClientRequestBookingDto dto);

    BookingDto ownerResponseToTheBooking(Long userId, Boolean approved, Long bookingId);

    BookingDto findBookingForAuthorOrOwner(Long userId, Long bookingId) throws RuntimeException;

    List<BookingDto> findAllBookingsForBooker(Long userId, String state);

    List<BookingDto> findAllBookingsForOwner(Long userId, String state);
}
