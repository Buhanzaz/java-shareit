package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Sql(scripts = {"file:src/main/resources/schema.sql"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemRequestServiceImplTest {

    final UserRepository userRepository;
    final ItemRequestServiceImpl itemRequestService;

    ItemRequestDto onlyDescriptionRequestDto;
    User firstUser;
    User secondUser;

    @BeforeEach
    void setUp() {
        firstUser = User.builder()
                .name("Test Name")
                .email("Test@email.com")
                .build();

        secondUser = User.builder()
                .name("Test Name 2")
                .email("Test2@email.com")
                .build();

        onlyDescriptionRequestDto = ItemRequestDto.builder()
                .description("Test Description")
                .build();
    }

    @Test
    void addItemRequestAnaSearchAllItemsRequestsById() {
        userRepository.save(firstUser);

        ItemRequestDto savedRequest = itemRequestService.addItemRequest(firstUser.getId(), onlyDescriptionRequestDto);
        ItemRequestDto findRequest = itemRequestService.searchAllItemsRequestsById(firstUser.getId(), savedRequest.getId());

        assertThat(savedRequest).usingRecursiveComparison().ignoringFields("items", "created")
                .isEqualTo(findRequest);
    }

    @Test
    void searchAllItemsRequestsCreator() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        ItemRequestDto savedRequest = itemRequestService.addItemRequest(secondUser.getId(), onlyDescriptionRequestDto);

        List<ItemRequestDto> privateRequests = itemRequestService
                .searchAllItemsRequestsCreator(secondUser.getId(), 0, 2);
        ItemRequestDto findRequest = itemRequestService.searchAllItemsRequestsById(secondUser.getId(), savedRequest.getId());

        assertThat(privateRequests.get(0)).usingRecursiveComparison().isEqualTo(findRequest);
    }

    @Test
    void searchAllItemsRequestsCreatorNotFoundUser() {
        firstUser.setId(1L);
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.searchAllItemsRequestsCreator(firstUser.getId(), 0, 2));

        assertEquals("Вы не зарегистрированы!", exception.getMessage());
    }

    @Test
    void searchAllItemsRequests() {
        userRepository.save(firstUser);
        userRepository.save(secondUser);
        ItemRequestDto savedRequest = itemRequestService.addItemRequest(firstUser.getId(), onlyDescriptionRequestDto);
        ItemRequestDto findRequest = itemRequestService.searchAllItemsRequestsById(firstUser.getId(), savedRequest.getId());

        List<ItemRequestDto> otherRequest = itemRequestService.searchAllItemsRequests(secondUser.getId(), 0, 2);

        assertThat(otherRequest.get(0)).usingRecursiveComparison().isEqualTo(findRequest);
    }

    @Test
    void searchAllItemsRequestsByIdNotFound() {
        firstUser = userRepository.save(firstUser);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.searchAllItemsRequestsById(firstUser.getId(), 1L));

        assertEquals(String.format("Запрос с id - %d, не найдет", 1L), exception.getMessage());
    }
}