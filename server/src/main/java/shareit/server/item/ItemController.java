package shareit.server.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.server.item.dto.CommentDto;
import shareit.server.item.dto.ItemDto;
import shareit.server.item.service.ItemService;
import shareit.server.validation.validationInterface.CreateValidationObject;
import shareit.server.validation.validationInterface.UpdateValidationObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    ItemService itemService;
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";
    private static final String URI_ID_ITEM = "/{itemId}";
    private static final String URI_SEARCH = "/search";
    private static final String URI_ADD_COMMENT = URI_ID_ITEM + "/comment";


    @PostMapping
    public ResponseEntity<ItemDto> postRequestItem(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                   @RequestBody @Validated(CreateValidationObject.class) ItemDto dto) {

        ItemDto itemDto = itemService.addItem(userId, dto);

        return ResponseEntity.ok(itemDto);
    }

    @PatchMapping(URI_ID_ITEM)
    public ResponseEntity<ItemDto> patchRequestItem(@PathVariable @Min(1) Long itemId,
                                                    @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                    @RequestBody @Validated(UpdateValidationObject.class) ItemDto dto) {

        ItemDto itemDto = itemService.updateItem(itemId, userId, dto);

        return ResponseEntity.ok(itemDto);
    }

    @GetMapping(URI_ID_ITEM)
    public ResponseEntity<ItemDto> getRequestItem(@PathVariable @Min(1) Long itemId,
                                                  @RequestHeader(HEADER_ID_USER) @Min(1) Long userId) {

        ItemDto itemDto = itemService.getItemById(itemId, userId);

        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getRequestItemsOwner(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId) {

        List<ItemDto> itemDto = itemService.getAllItemsOwner(userId);

        return ResponseEntity.ok(itemDto);
    }

    @GetMapping(URI_SEARCH)
    public ResponseEntity<List<ItemDto>> getRequestItemSearch(
            @RequestParam(name = "text") String itemName,
            @RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 20) Integer size) {

        List<ItemDto> itemDto = itemService.itemSearch(itemName, userId, from, size);

        return ResponseEntity.ok(itemDto);
    }

    @PostMapping(URI_ADD_COMMENT)
    public ResponseEntity<CommentDto> postRequestAddComment(@RequestHeader(HEADER_ID_USER) @Min(1) Long userId,
                                                            @PathVariable @Min(1) Long itemId,
                                                            @RequestBody @Validated CommentDto dto) {

        CommentDto commentDto = itemService.addComment(userId, itemId, dto);

        return ResponseEntity.ok(commentDto);
    }
}
