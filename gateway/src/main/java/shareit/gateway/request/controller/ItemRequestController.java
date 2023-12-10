package shareit.gateway.request.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.gateway.request.client.ItemRequestClient;
import shareit.gateway.request.dto.ItemRequestDto;
import shareit.gateway.validation.interfaces.CreateValidationObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

import static shareit.gateway.constant.Constants.HEADER_ID_USER;


@Validated
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient webClient;

    private static final String URI_SEARCH_ALL = "/all";
    private static final String URI_SEARCH_ITEMS_REQUESTS = "/{requestId}";

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

    @GetMapping(URI_SEARCH_ALL)
    public ResponseEntity<?> searchAllItemsRequests(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 20) Integer size) {

        return webClient.searchAllItemsRequests(userId, from, size);
    }

    @GetMapping(URI_SEARCH_ITEMS_REQUESTS)
    public ResponseEntity<?> searchAllItemsRequestsByI(
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @PathVariable @Min(1) Long requestId) {

        return webClient.searchAllItemsRequestsById(userId, requestId);
    }
}
