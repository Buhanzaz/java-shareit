package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Pages.getPageForItemRequest;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;
    ItemRequestMapper itemRequestMapper;


    @Override
    public ItemRequestDto addItemRequest(Long creatorId, ItemRequestDto dto) {
        User creator = userRepository.findById(creatorId).orElseThrow(() -> new NotFoundException("Вы не зарегистрированы!"));

        ItemRequest saveItemRequest = itemRequestRepository.save(itemRequestMapper.toModel(dto, creator));

        return itemRequestMapper.toDto(saveItemRequest);
    }

    @Override
    public List<ItemRequestDto> searchAllItemsRequestsCreator(Long creatorId, Integer from, Integer size) {
        validationUser(creatorId);

        List<ItemRequest> itemRequests = itemRequestRepository
                .searchItemRequestByCreatorId(creatorId, getPageForItemRequest(from, size));

        return itemRequests.stream().map(itemRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> searchAllItemsRequests(Long userId, Integer from, Integer size) {
        validationUser(userId);

        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(getPageForItemRequest(from, size));

        return itemRequestPage.stream()
                .filter(itemRequest -> !Objects.equals(itemRequest.getCreator().getId(), userId))
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto searchAllItemsRequestsById(Long userId, Long requestId) {
        validationUser(userId);
        ItemRequest itemRequest = itemRequestRepository
                .searchItemRequestById(requestId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Запрос с id - %d, не найдет", requestId))
                );

        return itemRequestMapper.toDto(itemRequest);
    }


    private void validationUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Вы не зарегистрированы!");
        }
    }


}

