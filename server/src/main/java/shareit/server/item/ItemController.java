package shareit.server.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shareit.server.item.dto.CommentDto;
import shareit.server.item.dto.ItemDto;
import shareit.server.item.service.ItemService;

import java.util.List;

import static shareit.server.constant.Constants.HEADER_ID_USER;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String URI_ID_ITEM = "/{itemId}";
    private static final String URI_SEARCH = "/search";
    private static final String URI_ADD_COMMENT = "/{itemId}/comment";


    @PostMapping
    public ResponseEntity<ItemDto> postRequestItem(@RequestHeader(HEADER_ID_USER) Long userId,
                                                   @RequestBody ItemDto dto) {

        ItemDto itemDto = itemService.addItem(userId, dto);

        return ResponseEntity.ok(itemDto);
    }

    @PatchMapping(URI_ID_ITEM)
    public ResponseEntity<ItemDto> patchRequestItem(@PathVariable Long itemId,
                                                    @RequestHeader(HEADER_ID_USER) Long userId,
                                                    @RequestBody ItemDto dto) {

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
    public ResponseEntity<List<ItemDto>> getRequestItemSearch(
            @RequestParam(name = "text") String itemName,
            @RequestHeader(HEADER_ID_USER) Long userId,
            Integer from, Integer size) {

        List<ItemDto> itemDto = itemService.itemSearch(itemName, userId, from, size);

        return ResponseEntity.ok(itemDto);
    }

    @PostMapping(URI_ADD_COMMENT)
    public ResponseEntity<CommentDto> postRequestAddComment(@RequestHeader(HEADER_ID_USER) Long userId,
                                                            @PathVariable Long itemId,
                                                            @RequestBody CommentDto dto) {

        CommentDto commentDto = itemService.addComment(userId, itemId, dto);

        return ResponseEntity.ok(commentDto);
    }
}
