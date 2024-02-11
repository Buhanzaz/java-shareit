package shareit.gateway.item.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.gateway.item.client.ItemClient;
import shareit.gateway.item.dto.CommentDto;
import shareit.gateway.item.dto.ItemDto;
import shareit.gateway.validation.interfaces.CreateValidationObject;
import shareit.gateway.validation.interfaces.UpdateValidationObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

import static shareit.gateway.constant.Constants.HEADER_ID_USER;

@Validated
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient webClient;
    private static final String URI_ID_ITEM = "/{itemId}";
    private static final String URI_SEARCH = "/search";
    private static final String URI_ADD_COMMENT = "/{itemId}/comment";


    @PostMapping
    public ResponseEntity<?> postRequestItem(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                             @RequestBody @Validated(CreateValidationObject.class) ItemDto dto) {
        return webClient.addItem(userId, dto);
    }

    @PatchMapping(URI_ID_ITEM)
    public ResponseEntity<?> patchRequestItem(@PathVariable @Min(1) Long itemId,
                                              @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                              @RequestBody @Validated(UpdateValidationObject.class) ItemDto dto) {
        return webClient.updateItem(itemId, userId, dto);
    }

    @GetMapping(URI_ID_ITEM)
    public ResponseEntity<?> getRequestItem(@PathVariable @Min(1) Long itemId,
                                            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId) {
        return webClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<?> getRequestItemsOwner(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId) {
        return webClient.getAllItemsOwner(userId);
    }

    @GetMapping(URI_SEARCH)
    public ResponseEntity<?> getRequestItemSearch(
            @RequestParam(name = "text") String itemName,
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 20) Integer size) {
        return webClient.itemSearch(itemName, userId, from, size);
    }

    @PostMapping(URI_ADD_COMMENT)
    public ResponseEntity<?> postRequestAddComment(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                   @PathVariable @Min(1) Long itemId,
                                                   @RequestBody @Validated(CreateValidationObject.class) CommentDto dto) {

        return webClient.addComment(userId, itemId, dto);
    }
}
