package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto dto);

    ItemDto updateItem(Long itemId, Long userid, ItemDto dto);

    ItemDto getItemById(Long itemId, Long userid);

    List<ItemDto> getAllItemsOwner(Long userId);

    List<ItemDto> itemSearch(String itemName, Long userid);

    CommentDto addComment(Long userid, Long itemId, CommentDto dto);

}
