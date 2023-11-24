package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Long creatorId, ItemRequestDto dto);

    List<ItemRequestDto> searchAllItemsRequestsCreator(Long creatorId);

    List<ItemRequestDto> searchAllItemsRequests(Long userId, Integer from, Integer size);

    ItemRequestDto searchAllItemsRequestsById(Long userId, Long requestId);
}
