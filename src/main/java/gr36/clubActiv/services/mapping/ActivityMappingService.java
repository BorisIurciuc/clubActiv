package gr36.clubActiv.services.mapping;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.Activity;
import gr36.clubActiv.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ActivityMappingService {


  @Mapping(source = "author.id", target = "authorId")
  @Mapping(target = "userIds", expression = "java(mapUserIds(entity.getUsers()))")
  ActivityDto mapEntityToDto(Activity entity);


  @Mapping(target = "author.id", source = "authorId")
  Activity mapDtoToEntity(ActivityDto dto);


  default List<Long> mapUserIds(List<User> users) {
    return users != null ? users.stream().map(User::getId).collect(Collectors.toList()) : null;
  }
}
