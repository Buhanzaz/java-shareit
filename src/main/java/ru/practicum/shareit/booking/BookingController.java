package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;
    private static final String URI_PATH_BOOKING_ID = "/{bookingId}";
    private static final String URI_PATH_BOOKINGS_FOR_OWNER = "/owner";
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<BookingDto> addNewBooking(@RequestHeader(HEADER_ID_USER) Long userId,
                                                    @RequestBody @Validated ClientRequestBookingDto dto) {
        return ResponseEntity.ok(bookingService.addNewBooking(userId, dto));
    }

    @PatchMapping(URI_PATH_BOOKING_ID)
    public ResponseEntity<BookingDto> ownerResponseToTheBooking(@RequestHeader(HEADER_ID_USER) Long userId,
                                                                @RequestParam Boolean approved,
                                                                @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.ownerResponseToTheBooking(userId, approved, bookingId));
    }

    @GetMapping(URI_PATH_BOOKING_ID)
    public ResponseEntity<BookingDto> findBookingByItemIdForAuthorBookingOrOwnerItem(@RequestHeader(HEADER_ID_USER) Long userId,
                                                                                     @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.findBookingForAuthorOrOwner(userId, bookingId));
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> findAllBookingsForUser(@RequestHeader(HEADER_ID_USER) Long userId,
                                                                   @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok(bookingService.findAllBookingsForBooker(userId, state));
    }

    @GetMapping(URI_PATH_BOOKINGS_FOR_OWNER)
    public ResponseEntity<List<BookingDto>> getAllBookingsForOwner(@RequestHeader(HEADER_ID_USER) Long userId,
                                                                   @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok(bookingService.findAllBookingsForOwner(userId, state));
    }
}
