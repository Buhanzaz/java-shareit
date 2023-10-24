package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public interface ItemService {

    ItemDto addItem(Long userId, ItemDto dto);

    ItemDto updateItem(Long itemId, Long userid, ItemDto dto);

    ItemDto getItemById(Long itemId, Long userid);

    List<ItemDto> getAllItemsOwner(Long userId);

    List<ItemDto> itemSearch(String itemName, Long userid);
}
