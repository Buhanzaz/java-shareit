package ru.practicum.shareit.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User ToModel(UserDto itemDto);

    UserDto ToDto(User user);
}
