package ru.practicum.shareit.item.service.db;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
import ru.practicum.shareit.item.repository.db.CommentRepositoryInDB;
import ru.practicum.shareit.item.repository.db.ItemRepositoryInDB;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserRepositoryInDB;

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
class ItemServiceImplInDB implements ItemService {

    ItemRepositoryInDB itemRepository;
    UserRepositoryInDB userRepository;
    BookingRepository bookingRepository;
    CommentRepositoryInDB commentRepository;

    ItemMapper itemMapper;
    CommentMapper commentMapper;
    BookingMapper bookingMapper;

    @Override
    public ItemDto addItem(Long userId, ItemDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Юзер с id " + userId + " не найден"));
        dto.setUser(user);
        return itemMapper.toDtoItem(itemRepository.save(itemMapper.toModelItem(dto)));
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto dto) {
        Item item = itemRepository.findItemByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        itemMapper.updateItem(item, dto);
        //itemRepository.save(item);

        return itemMapper.toDtoItem(item);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));


        ItemDto itemDto = itemMapper.toDtoItem(item);
        if (item.getUser().getId() == userId) {
            LocalDateTime now = LocalDateTime.now();
            List<Booking> bookings = bookingRepository.findByItem_IdAndStatusNotIn(
                    itemId, List.of(Status.REJECTED, Status.CANCELED));

            bookings.stream().filter(Booking ->
                            Booking.getStart().isBefore(now)).max(Comparator.comparing(Booking::getEnd))
                    .ifPresent(lastBooking -> itemDto.setLastBooking(bookingMapper.toWithoutItemDto(lastBooking)));

            bookings.stream().filter(Booking ->
                            Booking.getStart().isAfter(now)).min(Comparator.comparing(Booking::getStart))
                    .ifPresent(nextBooking -> itemDto.setNextBooking(bookingMapper.toWithoutItemDto(nextBooking)));


        }
        List<Comment> comments = commentRepository.findByItem_Id(itemId);

        List<CommentDto> commentDtos = comments.stream().map(commentMapper::toDto)
                .collect(Collectors.toList());
        itemDto.setComments(commentDtos);
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItemsOwner(Long userId) {
        List<Item> items = itemRepository.findAllItemByUser(userId);

        List<Booking> bookings = bookingRepository.findByItem_UserId(userId);
        List<ItemDto> collectDto = items.stream().map(itemMapper::toDtoItem).collect(Collectors.toList());
        LocalDateTime now = LocalDateTime.now();
        for (ItemDto itemDto : collectDto) {
            List<Booking> itemBookings = bookings.stream()
                    .filter(Booking -> Objects.equals(Booking.getItem().getId(), itemDto.getId())
                            && Booking.getStatus() != Status.REJECTED)
                    .collect(Collectors.toList());

            itemBookings.stream().filter(Booking ->
                            Booking.getStart().isBefore(now)).max(Comparator.comparing(Booking::getEnd))
                    .ifPresent(lastBooking -> itemDto.setLastBooking(bookingMapper.toWithoutItemDto(lastBooking)));

            itemBookings.stream().filter(Booking ->
                            Booking.getStart().isAfter(now)).min(Comparator.comparing(Booking::getStart))
                    .ifPresent(nextBooking -> itemDto.setNextBooking(bookingMapper.toWithoutItemDto(nextBooking)));
        }
        return collectDto.stream().sorted(Comparator.comparing(ItemDto::getId)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> itemSearch(String text, Long userid) {
        if (text.isBlank() || text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItem(text).stream()
                .map(itemMapper::toDtoItem)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto dto) throws RuntimeException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Вы не зарегестрированы."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещи с id : %d не существует.", itemId)));
        List<Booking> bookings = bookingRepository.findByItem_IdAndBooker_IdAndStatusAndEndBefore(
                itemId, userId, Status.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new BookingException("Вы не можете ставить комментарии под вещью которую не бронировали ранее.");
        }

        Comment comment = commentMapper.toModel(dto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        Comment save = commentRepository.save(comment);
        return commentMapper.toDto(save);
    }

    /*private BookingForItem getBookingForItem(Long itemId) {
        List<BookingDto> bookingDtoList = bookingRepository.findBookingsByItem_Id(itemId)
                .stream().map(bookingMapper::toDtoItem)
                .collect(Collectors.toList());
        BookingForItem bookingForItem = new BookingForItem();
        bookingForItem.setLastBooking(
                bookingDtoList.stream()
                        .filter(bookingStream -> !(bookingStream.getStatus().equals(Status.REJECTED)))
                        .filter(bookingStream -> bookingStream.getStart().isBefore(LocalDateTime.now()))
                        .min(Comparator.comparing(BookingDto::getStart)).orElse(null));

        bookingForItem.setNextBooking(
                bookingDtoList.stream()
                        .filter(bookingStream -> !(bookingStream.getStatus().equals(Status.REJECTED)))
                        .filter(bookingStream -> bookingStream.getStart().isAfter(LocalDateTime.now()))
                        .min(Comparator.comparing(BookingDto::getStart)).orElse(null));
        return bookingForItem;
    }*/
    private Booking getBookingForItem(Long itemId) {
        List<Booking> bookingDtoList = bookingRepository.findBookingsByItem_Id(itemId);
        boolean seen = false;
        Booking best = null;
        Comparator<Booking> comparator = Comparator.comparing(Booking::getStart);
        for (Booking bookingStream : bookingDtoList) {
            if (!(bookingStream.getStatus().equals(Status.REJECTED))) {
                if (bookingStream.getStart().isBefore(LocalDateTime.now())) {
                    if (!seen || comparator.compare(bookingStream, best) < 0) {
                        seen = true;
                        best = bookingStream;
                    }
                }
            }
        }
        return seen ? best : null;
    }
}
