package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ItemServiceImpl implements ItemService {

    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;

    ItemMapper itemMapper;
    CommentMapper commentMapper;
    BookingMapper bookingMapper;

    Sort sortBy = Sort.by(Sort.Direction.ASC, "id");

    @Override
    @Transactional
    public ItemDto addItem(Long userId, ItemDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Юзер с id " + userId + " не найден")
        );

        Item modelItem = itemMapper.toModelItem(dto);

        if (dto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(dto.getRequestId()).orElseThrow(
                    () -> new NotFoundException(String.format("Запроса на создание вещи с id %d не найдено",
                            dto.getRequestId()))
            );

            modelItem.setItemRequest(itemRequest);
        }

        modelItem.setUser(user);

        return itemMapper.toDtoItem(itemRepository.save(modelItem));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, Long userId, ItemDto dto) {
        Item item = itemRepository.findItemByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        itemMapper.updateItem(item, dto);

        return itemMapper.toDtoItem(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long itemId, Long userId) {
        //TODO Подумать над новой реализацией
        /*Первая причина видится в использовании кучи запросов что можно изменить, так же думаю что и памяти тратится оч много*/
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        ItemDto itemDto = itemMapper.toDtoItem(item);

        if (item.getUser().getId() == userId) {
            List<Booking> bookings = bookingRepository.findByItem_IdAndStatusNotIn(
                    itemId, List.of(Status.REJECTED, Status.CANCELED));

            addLastAndNextBooking(itemDto, bookings);
        }

        List<CommentDto> commentsDto = commentRepository.findByItem_Id(itemId).stream()
                .map(commentMapper::commentToDto)
                .collect(Collectors.toList());

        itemDto.setComments(commentsDto);
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllItemsOwner(Long userId) {
        //TODO Подумать над новой реализацией
        /*Первая причина видится в использовании кучи запросов что можно изменить, так же думаю что и памяти тратится оч много*/
        List<Item> items = itemRepository.findAllItemByUser(userId);
        List<Booking> bookings = bookingRepository.findByItem_UserId(userId);
        List<ItemDto> itemsDto = items.stream().map(itemMapper::toDtoItem).collect(Collectors.toList());
        List<Comment> comments = commentRepository.findByItem_UserId(userId);

        for (ItemDto itemDto : itemsDto) {
            List<Booking> itemBookings = bookings.stream()
                    .filter(Booking -> Objects.equals(Booking.getItem().getId(), itemDto.getId())
                            && Booking.getStatus() != Status.REJECTED)
                    .collect(Collectors.toList());

            addLastAndNextBooking(itemDto, itemBookings);

            List<CommentDto> commentsDto = comments.stream()
                    .filter(comment -> Objects.equals(comment.getItem().getId(), itemDto.getId()))
                    .map(commentMapper::commentToDto)
                    .collect(Collectors.toList());

            itemDto.setComments(commentsDto);
        }

        return itemsDto.stream().sorted(Comparator.comparing(ItemDto::getId)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> itemSearch(String text, Long userId, Integer from, Integer size) {
        if (text.isBlank() || text.isEmpty()) {
            return Collections.emptyList();
        }
        Pageable page = PageRequest.of(from / size, size, sortBy);

        return itemRepository.searchItem(text, page).stream()
                .map(itemMapper::toDtoItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto dto) throws RuntimeException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Вы не зарегистрированы."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещи с id : %d не существует.", itemId)));
        List<Booking> bookings = bookingRepository.findByItem_IdAndBooker_IdAndStatusAndEndBefore(
                itemId, userId, Status.APPROVED, LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new BookingException("Вы не можете ставить комментарии под вещью которую не бронировали ранее.");
        }

        Comment comment = commentMapper.commentDtoToModel(dto, user, item, LocalDateTime.now());
        Comment save = commentRepository.save(comment);
        return commentMapper.commentToDto(save);
    }

    private void addLastAndNextBooking(ItemDto itemDto, List<Booking> itemBookings) {
        LocalDateTime now = LocalDateTime.now();

        itemBookings.stream().filter(Booking ->
                        Booking.getStart().isBefore(now)).max(Comparator.comparing(Booking::getEnd))
                .ifPresent(lastBooking -> itemDto.setLastBooking(bookingMapper.bookingToWithoutItemDto(lastBooking)));

        itemBookings.stream().filter(Booking ->
                        Booking.getStart().isAfter(now)).min(Comparator.comparing(Booking::getStart))
                .ifPresent(nextBooking -> itemDto.setNextBooking(bookingMapper.bookingToWithoutItemDto(nextBooking)));
    }
}
