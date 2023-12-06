package shareit.gateway.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.gateway.request.dto.ItemRequestDto;
import shareit.gateway.validation.CreateValidationObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-item-requests.
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemRequestController {

    ItemRequestClient webClient;
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<?> addItemRequest(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long creatorId,
            @RequestBody @Validated(CreateValidationObject.class) ItemRequestDto dto) {

        return webClient.addItemRequest(creatorId, dto);
    }

    @GetMapping
    public ResponseEntity<?> findAllItemsRequestsCreator(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long creatorId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 20) Integer size) {

        return webClient.findAllItemsRequestsCreator(creatorId, from, size);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> searchAllItemsRequests(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 20) Integer size) {

        return webClient.searchAllItemsRequests(userId, from, size);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<?> searchAllItemsRequestsByI(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @PathVariable @Min(1) Long requestId) {

        return webClient.searchAllItemsRequestsById(userId, requestId);
    }
}
