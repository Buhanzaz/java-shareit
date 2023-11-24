package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemRequestController {

    ItemRequestService itemRequestService;
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemRequestDto> postRequestAddItemRequest(
            @RequestHeader(HEADER_ID_USER) Long creatorId,
            @RequestBody @Validated(CreateValidationObject.class) ItemRequestDto dto) {
        return ResponseEntity.ok(itemRequestService.addItemRequest(creatorId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getMappingSearchAllItemsRequestsCreator(
            @RequestHeader(HEADER_ID_USER) Long creatorId) {
        return ResponseEntity.ok(itemRequestService.searchAllItemsRequestsCreator(creatorId));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ItemRequestDto>> getMappingSearchAllItemsRequests(@RequestHeader(HEADER_ID_USER) Long userId,
                                                                           @RequestParam(defaultValue = "0") Integer from,
                                                                           @RequestParam(defaultValue = "10")  Integer size) {
        return ResponseEntity.ok(itemRequestService.searchAllItemsRequests(userId, from, size));
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<ItemRequestDto> getMappingSearchItemsRequestsById(@RequestHeader(HEADER_ID_USER) Long userId,
                                                                            @PathVariable Long requestId) {
        return ResponseEntity.ok(itemRequestService.searchAllItemsRequestsById(userId, requestId));
    }
}
