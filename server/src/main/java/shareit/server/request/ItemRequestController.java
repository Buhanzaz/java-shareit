package shareit.server.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shareit.server.request.dto.ItemRequestDto;
import shareit.server.request.service.ItemRequestService;

import java.util.List;

import static shareit.server.constant.Constants.HEADER_ID_USER;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String URI_SEARCH_ALL = "/all";
    private static final String URI_SEARCH_ITEMS_REQUESTS = "/{requestId}";

    @PostMapping
    public ResponseEntity<ItemRequestDto> postRequestAddItemRequest(
            @RequestHeader(HEADER_ID_USER) Long creatorId,
            @RequestBody ItemRequestDto dto) {

        return ResponseEntity.ok(itemRequestService.addItemRequest(creatorId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getMappingSearchAllItemsRequestsCreator(
            @RequestHeader(HEADER_ID_USER) Long creatorId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        return ResponseEntity.ok(itemRequestService.searchAllItemsRequestsCreator(creatorId, from, size));
    }

    @GetMapping(URI_SEARCH_ALL)
    public ResponseEntity<List<ItemRequestDto>> getMappingSearchAllItemsRequests(
            @RequestHeader(HEADER_ID_USER) Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        return ResponseEntity.ok(itemRequestService.searchAllItemsRequests(userId, from, size));
    }

    @GetMapping(URI_SEARCH_ITEMS_REQUESTS)
    public ResponseEntity<ItemRequestDto> getMappingSearchItemsRequestsById(
            @RequestHeader(HEADER_ID_USER) Long userId,
            @PathVariable Long requestId) {

        return ResponseEntity.ok(itemRequestService.searchAllItemsRequestsById(userId, requestId));
    }
}
