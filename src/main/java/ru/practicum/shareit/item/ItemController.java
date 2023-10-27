package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    ItemService itemService;
    private final String HEADER_ID_USER = "X-Sharer-User-Id";
    private final String URI_ID_ITEM = "/{itemId}";
    private final String URI_SEARCH = "/search";

    @PostMapping
    public ResponseEntity<ItemDto> postRequestItem(@RequestHeader(HEADER_ID_USER) Long userId,
                                                   @RequestBody @Validated(CreateValidationObject.class) ItemDto dto) {
        ItemDto itemDto = itemService.addItem(userId, dto);
        return ResponseEntity.ok(itemDto);
    }

    @PatchMapping(path = URI_ID_ITEM)
    public ResponseEntity<ItemDto> patchRequestItem(@PathVariable Long itemId,
                                                    @RequestHeader(HEADER_ID_USER) Long userid,
                                                    @RequestBody @Validated(UpdateValidationObject.class) ItemDto dto) {
        ItemDto itemDto = itemService.updateItem(itemId, userid, dto);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping(path = URI_ID_ITEM)
    public ResponseEntity<ItemDto> getRequestItem(@PathVariable Long itemId,
                                                  @RequestHeader(HEADER_ID_USER) Long userid) {
        ItemDto itemDto = itemService.getItemById(itemId, userid);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getRequestItemsOwner(@RequestHeader(HEADER_ID_USER) Long userid) {
        List<ItemDto> dtoItems = itemService.getAllItemsOwner(userid);
        return ResponseEntity.ok(dtoItems);
    }

    @GetMapping(path = URI_SEARCH)
    public ResponseEntity<List<ItemDto>> getRequestItemSearch(@RequestParam(name = "text") String itemName,
                                                              @RequestHeader(HEADER_ID_USER) Long userid) {
        List<ItemDto> itemDto = itemService.itemSearch(itemName, userid);
        return ResponseEntity.ok(itemDto);
    }
}
