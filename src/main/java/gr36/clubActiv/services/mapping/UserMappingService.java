package gr36.clubActiv.services.mapping;

import gr36.clubActiv.domain.dto.UserDto;
import gr36.clubActiv.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMappingService {


    @Mapping(target = "password", ignore = true) // Игнорируем пароль при маппинге
    User mapDtoToEntity(UserDto dto);


    UserDto mapEntityToDto(User user);




}
