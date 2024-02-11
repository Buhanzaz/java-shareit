package shareit.server.request.service;

import shareit.server.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Long creatorId, ItemRequestDto dto);

    List<ItemRequestDto> searchAllItemsRequestsCreator(Long creatorId, Integer from, Integer size);

    List<ItemRequestDto> searchAllItemsRequests(Long userId, Integer from, Integer size);

    ItemRequestDto searchAllItemsRequestsById(Long userId, Long requestId);
}
