package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.validation.validationInterface.Validation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    ItemMapper itemMapper;
    Validation validation;

    @Override
    public ItemDto addItem(Long userId, ItemDto dto) {
        Item item = itemMapper.toModel(dto);

        validation.checksUserId(userId);
        item.setUserId(userId);

        return itemMapper.toDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userid, ItemDto dto) {
        validation.checksUserId(userid);
        validation.checksItemId(itemId);
        validation.checksItemOwnership(itemId, userid);

        Item item = getItemByIdNotDto(itemId);

        itemMapper.updateItem(item, dto);
        return itemMapper.toDto(item);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userid) {
        validation.checksUserId(userid);
        validation.checksItemId(itemId);

        return itemMapper.toDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsOwner(Long userid) {
        validation.checksUserId(userid);

        List<Item> items = itemRepository.getAllItemsOwner(userid);

        return items.stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> itemSearch(String itemName, Long userid) {
        validation.checksUserId(userid);

        if (itemName.isBlank() || itemName.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.itemSearch(itemName);

        return items.stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    private Item getItemByIdNotDto(Long itemId) {
        return itemRepository.getItemById(itemId);
    }
}