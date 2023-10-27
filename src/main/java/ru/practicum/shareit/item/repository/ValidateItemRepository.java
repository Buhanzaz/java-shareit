package ru.practicum.shareit.item.repository;

public interface ValidateItemRepository {
    boolean isItemInBd(Long itemId);

    boolean isItemOwner(Long itemId, Long userid);
}
