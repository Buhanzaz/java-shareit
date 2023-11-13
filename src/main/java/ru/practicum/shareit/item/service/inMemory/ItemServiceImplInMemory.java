package ru.practicum.shareit.item.service.inMemory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.inMemory.ItemRepositoryInMemory;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.inMenory.UserRepositoryInMemory;
import ru.practicum.shareit.validation.validationInterface.Validation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ItemServiceImplInMemory {

    ItemRepositoryInMemory itemRepositoryInMemory;
    UserRepositoryInMemory userRepositoryInMemory;
    ItemMapper itemMapper;
    Validation validation;

    @Override
    public ItemDto addItem(Long userId, ItemDto dto) {
        validation.checksUserId(userId);
        Item item = itemMapper.toModelItem(dto);
        User user = userRepositoryInMemory.getUserById(userId);

        item.setUser(user);

        return itemMapper.toDtoItem(itemRepositoryInMemory.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userid, ItemDto dto) {
        validation.checksUserId(userid);
        validation.checksItemId(itemId);
        validation.checksItemOwnership(itemId, userid);

        Item item = getItemByIdWithoutDto(itemId);

        itemMapper.updateItem(item, dto);
        return itemMapper.toDtoItem(item);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userid) {
        validation.checksUserId(userid);
        validation.checksItemId(itemId);

        return itemMapper.toDtoItem(itemRepositoryInMemory.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsOwner(Long userid) {
        validation.checksUserId(userid);

        List<Item> items = itemRepositoryInMemory.getAllItemsOwner(userid);

        return items.stream().map(itemMapper::toDtoItem).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> itemSearch(String itemName, Long userid) {
        validation.checksUserId(userid);

        if (itemName.isBlank() || itemName.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepositoryInMemory.itemSearch(itemName).stream()
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());

        return items.stream().map(itemMapper::toDtoItem).collect(Collectors.toList());
    }

    private Item getItemByIdWithoutDto(Long itemId) {
        return itemRepositoryInMemory.getItemById(itemId);
    }
}*/
