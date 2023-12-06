package shareit.gateway.booking.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.gateway.booking.client.BookingClient;
import shareit.gateway.booking.dto.ClientRequestBookingDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingClient webClient;
    private static final String URI_PATH_BOOKING_ID = "/{bookingId}";
    private static final String URI_PATH_BOOKINGS_FOR_OWNER = "/owner";
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<?> addNewBooking(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                           @RequestBody @Validated ClientRequestBookingDto dto) {

        return webClient.addNewBooking(userId, dto);
    }

    @PatchMapping(URI_PATH_BOOKING_ID)
    public ResponseEntity<?> ownerResponseToTheBooking(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                       @RequestParam Boolean approved,
                                                       @PathVariable @Min(1) Long bookingId) {

        return webClient.ownerResponseToTheBooking(userId, approved, bookingId);
    }

    @GetMapping(URI_PATH_BOOKING_ID)
    public ResponseEntity<?> findBookingByItemIdForAuthorBookingOrOwnerItem(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @PathVariable @Min(1) Long bookingId) {

        return webClient.findBookingForAuthorOrOwner(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<?> findAllBookingsForUser(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "20", required = false) @Range(min = 1, max = 20) Integer size) {

        return webClient.findAllBookingsForBooker(userId, state, from, size);
    }

    @GetMapping(URI_PATH_BOOKINGS_FOR_OWNER)
    public ResponseEntity<?> getAllBookingsForOwner(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "20", required = false) @Range(min = 1, max = 20) Integer size) {

        return webClient.findAllBookingsForOwner(userId, state, from, size);
    }
}
