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

    @PostMapping
    public ResponseEntity<ItemDto> postRequestItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestBody @Validated(CreateValidationObject.class) ItemDto dto) {
        ItemDto itemDto = itemService.addItem(userId, dto);
        return ResponseEntity.ok(itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> patchRequestItem(@PathVariable Long itemId,
                                                    @RequestHeader("X-Sharer-User-Id") Long userid,
                                                    @RequestBody @Validated(UpdateValidationObject.class)ItemDto dto) {
        ItemDto itemDto = itemService.updateItem(itemId, userid, dto);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getRequestItem(@PathVariable Long itemId,
                                                  @RequestHeader("X-Sharer-User-Id") Long userid) {
        ItemDto itemDto = itemService.getItemById(itemId, userid);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getRequestItemsOwner(@RequestHeader("X-Sharer-User-Id") Long userid) {
        List<ItemDto> dtoItems = itemService.getAllItemsOwner(userid);
        return ResponseEntity.ok(dtoItems);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getRequestItemSearch(@RequestParam(name = "text") String itemName,
                                                        @RequestHeader("X-Sharer-User-Id") Long userid) {
        List<ItemDto> itemDto = itemService.itemSearch(itemName, userid);
        return ResponseEntity.ok(itemDto);
    }
}
