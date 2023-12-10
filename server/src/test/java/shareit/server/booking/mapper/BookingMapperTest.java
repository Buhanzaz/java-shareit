package shareit.server.booking.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import shareit.server.booking.model.Booking;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class BookingMapperTest {
    @Spy
    BookingMapper bookingMapper;

    @Test
    void clientRequestDtoToModelNull() {
        Optional<Booking> booking = Optional.ofNullable(bookingMapper.clientRequestDtoToModel(null, null, null, null));

        assertFalse(booking.isPresent());
    }
}