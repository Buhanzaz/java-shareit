package ru.practicum.shareit.item.service.db;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.db.ItemRepositoryInDB;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepositoryInDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ItemServiceImplInDB implements ItemService {

    ItemRepositoryInDB itemRepository;
    UserRepositoryInDB userRepository;
    ItemMapper itemMapper;

    @Override
    public ItemDto addItem(Long userId, ItemDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Юзер с id " + userId + " не найден"));
        dto.setUser(user);
        return itemMapper.toDto(itemRepository.save(itemMapper.toModel(dto)));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto dto) {
        Item item = itemRepository.findItemByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        itemMapper.updateItem(item, dto);
        //itemRepository.save(item);

        return itemMapper.toDto(item);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userid) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsOwner(Long userId) {
        return itemRepository.findAllItemByUser(userId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> itemSearch(String text, Long userid) {
        if (text.isBlank() || text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItem(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}
