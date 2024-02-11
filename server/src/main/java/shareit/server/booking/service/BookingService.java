package shareit.server.booking.service;

import shareit.server.booking.dto.BookingDto;
import shareit.server.booking.dto.ClientRequestBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(Long userId, ClientRequestBookingDto dto);

    BookingDto ownerResponseToTheBooking(Long userId, Boolean approved, Long bookingId);

    BookingDto findBookingForAuthorOrOwner(Long userId, Long bookingId);

    List<BookingDto> findAllBookingsForBooker(Long userId, String state, Integer from, Integer size);

    List<BookingDto> findAllBookingsForOwner(Long userId, String state, Integer from, Integer size);
}
