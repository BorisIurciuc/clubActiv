package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.User;

import java.util.List;

public interface ActivityService {

  ActivityDto create(ActivityDto activityDto, User author);

  List<ActivityDto> getAllActivities();

  ActivityDto update(Long id, ActivityDto dto);

  ActivityDto getActivityById(Long id);

  void deleteActivity(Long id);

  ActivityDto addUserToActivity(Long activityId, String username);

  List<ActivityDto> getActivitiesByUserId(Long userId);

  ActivityDto removeUserFromActivity(Long activityId, String username);
}
