package shareit.server.user.mapper;

import org.mapstruct.*;
import shareit.server.user.dto.UserDto;
import shareit.server.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(UserDto userDto);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserDto userDto);
}

