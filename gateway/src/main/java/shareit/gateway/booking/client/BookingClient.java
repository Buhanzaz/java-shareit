package shareit.gateway.booking.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.gateway.booking.dto.ClientRequestBookingDto;
import shareit.gateway.validation.Validator;
import shareit.gateway.webClient.BasicWebClient;

import java.util.Map;

@Component
public class BookingClient extends BasicWebClient {

    private static final String API_BOOKING_LOCATION = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_BOOKING_LOCATION))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<?> addNewBooking(Long userId, ClientRequestBookingDto dto) {
        Validator.validationTimeFromDto(dto);

        return post(dto, userId);
    }

    public ResponseEntity<?> ownerResponseToTheBooking(Long userId, Boolean approved, Long bookingId) {
        return update(String.format("/%d?approved=%b", bookingId, approved), userId);
    }

    public ResponseEntity<?> findBookingForAuthorOrOwner(Long userId, Long bookingId) {
        return get(String.format("/%d", bookingId), userId);
    }

    public ResponseEntity<?> findAllBookingsForBooker(Long userId, String state, Integer from, Integer size) {
        Validator.validationTypeBooking(state);

        Map<String, Object> param = Map.of(
                "state", state,
                "from", from,
                "size", size
        );

        return get("?state={state}&from={from}&size={size}", param, userId);
    }

    public ResponseEntity<?> findAllBookingsForOwner(Long userId, String state, Integer from, Integer size) {
        Validator.validationTypeBooking(state);

        Map<String, Object> param = Map.of(
                "state", state,
                "from", from,
                "size", size
        );

        return get("/owner?state={state}&from={from}&size={size}", param, userId);
    }
}
