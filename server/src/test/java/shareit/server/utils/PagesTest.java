package shareit.server.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PagesTest {
    @Test
    void getPageForBooking() {
        Pageable pageForBooking = Pages.getPageForBooking(0, 1);

        assertEquals(0, pageForBooking.getPageNumber());
        assertEquals(1, pageForBooking.getPageSize());
        assertEquals("start: DESC", pageForBooking.getSort().toString());
    }

    @Test
    void getPageForItemRequest() {
        Pageable pageForBooking = Pages.getPageForItemRequest(0, 1);

        assertEquals(0, pageForBooking.getPageNumber());
        assertEquals(1, pageForBooking.getPageSize());
        assertEquals("created: DESC", pageForBooking.getSort().toString());
    }

    @Test
    void getPageForItem() {
        Pageable pageForBooking = Pages.getPageForItem(0, 1);

        assertEquals(0, pageForBooking.getPageNumber());
        assertEquals(1, pageForBooking.getPageSize());
        assertEquals("id: ASC", pageForBooking.getSort().toString());
    }
}