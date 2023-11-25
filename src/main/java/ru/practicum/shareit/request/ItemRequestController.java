package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemRequestController {

    ItemRequestService itemRequestService;
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";


    @PostMapping
    public ResponseEntity<ItemRequestDto> postRequestAddItemRequest(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long creatorId,
            @RequestBody @Validated(CreateValidationObject.class) ItemRequestDto dto) {

        return ResponseEntity.ok(itemRequestService.addItemRequest(creatorId, dto));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getMappingSearchAllItemsRequestsCreator(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long creatorId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 20) Integer size) {

        return ResponseEntity.ok(itemRequestService.searchAllItemsRequestsCreator(creatorId, from, size));
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ItemRequestDto>> getMappingSearchAllItemsRequests(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 20) Integer size) {

        return ResponseEntity.ok(itemRequestService.searchAllItemsRequests(userId, from, size));
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<ItemRequestDto> getMappingSearchItemsRequestsById(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                                            @PathVariable @Min(1) Long requestId) {

        return ResponseEntity.ok(itemRequestService.searchAllItemsRequestsById(userId, requestId));
    }
}
