package shareit.server.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public final class Pages {
    public static Pageable getPageForBooking(Integer from, Integer size) {
        return PageRequest.of(from / size, size, Sort.by(Direction.DESC, "start"));
    }

    public static Pageable getPageForItemRequest(Integer from, Integer size) {
        return PageRequest.of(from / size, size, Sort.by(Direction.DESC, "created"));
    }

    public static Pageable getPageForItem(Integer from, Integer size) {
        return PageRequest.of(from / size, size, Sort.by(Direction.ASC, "id"));
    }
}
