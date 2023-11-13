package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.validationInterface.CreateValidationObject;
import ru.practicum.shareit.validation.validationInterface.UpdateValidationObject;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    ItemService itemService;
    private static final String HEADER_ID_USER = "X-Sharer-User-Id";
    private static final String URI_ID_ITEM = "/{itemId}";
    private static final String URI_SEARCH = "/search";
    private static final String URI_ADD_COMMENT = URI_ID_ITEM + "/comment";

    public ItemController(@Qualifier("itemServiceImplInDB") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> postRequestItem(@RequestHeader(HEADER_ID_USER) Long userId,
                                                   @RequestBody @Validated(CreateValidationObject.class) ItemDto dto) {
        ItemDto itemDto = itemService.addItem(userId, dto);
        return ResponseEntity.ok(itemDto);
    }

    @PatchMapping(URI_ID_ITEM)
    public ResponseEntity<ItemDto> patchRequestItem(@PathVariable Long itemId,
                                                    @RequestHeader(HEADER_ID_USER) Long userId,
                                                    @RequestBody @Validated(UpdateValidationObject.class) ItemDto dto) {
        ItemDto itemDto = itemService.updateItem(itemId, userId, dto);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping(URI_ID_ITEM)
    public ResponseEntity<ItemDto> getRequestItem(@PathVariable Long itemId,
                                                  @RequestHeader(HEADER_ID_USER) Long userId) {
        ItemDto itemDto = itemService.getItemById(itemId, userId);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getRequestItemsOwner(@RequestHeader(HEADER_ID_USER) Long userId) {
        List<ItemDto> itemDto = itemService.getAllItemsOwner(userId);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping(URI_SEARCH)
    public ResponseEntity<List<ItemDto>> getRequestItemSearch(@RequestParam(name = "text") String itemName,
                                                              @RequestHeader(HEADER_ID_USER) Long userId) {
        List<ItemDto> itemDto = itemService.itemSearch(itemName, userId);
        return ResponseEntity.ok(itemDto);
    }

    @PostMapping(URI_ADD_COMMENT)
    public ResponseEntity<CommentDto> postRequestAddComment(@RequestHeader(HEADER_ID_USER) Long userId,
                                                            @PathVariable Long itemId,
                                                            @RequestBody @Validated CommentDto dto) {
        CommentDto commentDto = itemService.addComment(userId, itemId, dto);
        return ResponseEntity.ok(commentDto);
    }
}
