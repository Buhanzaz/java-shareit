package shareit.geteway.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.geteway.item.client.ItemClient;
import shareit.geteway.item.dto.CommentDto;
import shareit.geteway.item.dto.ItemDto;
import shareit.geteway.validation.CreateValidationObject;
import shareit.geteway.validation.UpdateValidationObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    ItemClient webClient;
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";
    private static final String URI_ID_ITEM = "/{itemId}";
    private static final String URI_SEARCH = "/search";
    private static final String URI_ADD_COMMENT = URI_ID_ITEM + "/comment";


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
                                                   @RequestBody @Validated CommentDto dto) {

        return webClient.addComment(userId, itemId, dto);
    }
}
