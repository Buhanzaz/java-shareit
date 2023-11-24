package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ItemRequestDto> searchAllItemsRequestsCreator(Long creatorId) {
        validationUser(creatorId);

        List<ItemRequest> itemRequests = itemRequestRepository.searchItemRequestByCreator_IdOrderByCreatedAsc(creatorId);

        return itemRequests.stream().map(itemRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> searchAllItemsRequests(Long userId, Integer from, Integer size) {
        validationRequestParam(from, size);

        validationUser(userId);

        Sort sortBy = Sort.by(Sort.Direction.ASC, "created");
        Pageable page = PageRequest.of(from, size, sortBy);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(page);

        return itemRequestPage.stream()
                .filter(itemRequest -> itemRequest.getCreator().getId() != userId)
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto searchAllItemsRequestsById(Long userId, Long requestId) {
        return null;
    }

    private void validationRequestParam(Integer from, Integer size) {
        if (from == null || from < 0) {
            throw new ValidateException("Неправильно заданы параметры.");
        }
        if (size == null || size < 0) {
            throw new ValidateException("Неправильно заданы параметры.");
        }
    }

    private void validationUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Вы не зарегистрированы!");
        }
    }

}
