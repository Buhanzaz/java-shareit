package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ClientRequestBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Validated
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
    public ResponseEntity<BookingDto> addNewBooking(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                    @RequestBody @Validated ClientRequestBookingDto dto) {

        return ResponseEntity.ok(bookingService.addNewBooking(userId, dto));
    }

    @PatchMapping(URI_PATH_BOOKING_ID)
    public ResponseEntity<BookingDto> ownerResponseToTheBooking(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                                @RequestParam Boolean approved,
                                                                @PathVariable @Min(1) Long bookingId) {

        return ResponseEntity.ok(bookingService.ownerResponseToTheBooking(userId, approved, bookingId));
    }

    @GetMapping(URI_PATH_BOOKING_ID)
    public ResponseEntity<BookingDto> findBookingByItemIdForAuthorBookingOrOwnerItem(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @PathVariable @Min(1) Long bookingId) {

        return ResponseEntity.ok(bookingService.findBookingForAuthorOrOwner(userId, bookingId));
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> findAllBookingsForUser(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "20", required = false) @Range(min = 1, max = 20) Integer size) {

        return ResponseEntity.ok(bookingService.findAllBookingsForBooker(userId, state, from, size));
    }

    @GetMapping(URI_PATH_BOOKINGS_FOR_OWNER)
    public ResponseEntity<List<BookingDto>> getAllBookingsForOwner(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "20", required = false) @Range(min = 1, max = 20) Integer size) {

        return ResponseEntity.ok(bookingService.findAllBookingsForOwner(userId, state, from, size));
    }
}
