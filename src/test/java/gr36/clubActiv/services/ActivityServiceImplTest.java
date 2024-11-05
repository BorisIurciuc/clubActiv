package gr36.clubActiv.services;


import gr36.Images;
import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.Activity;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.ActivityCreationException;
import gr36.clubActiv.exeption_handling.exeptions.ActivityNotFoundException;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.repository.ActivityRepository;
import gr36.clubActiv.repository.UserRepository;
import gr36.clubActiv.services.interfaces.ActivityService;
import gr36.clubActiv.services.mapping.ActivityMappingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceImplTest {

  @Mock
  private ActivityRepository activityRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ActivityMappingService mappingService;

  @Mock
  private Images images;

  @InjectMocks
  private ActivityServiceImpl activityService;

  private Activity activity;
  private User user;
  private ActivityDto activityDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
    user.setUsername("testUser");

    activity = new Activity();
    activity.setId(1L);
    activity.setTitle("Test Activity");
    activity.setAuthor(user);

    activityDto = new ActivityDto();
    activityDto.setTitle("Test Activity");
  }

  @Test
  void testCreateActivity() {
    when(activityRepository.findByTitle(activityDto.getTitle())).thenReturn(Optional.empty());
    when(images.getRandomImage()).thenReturn("randomImageUrl");
    when(activityRepository.save(any(Activity.class))).thenReturn(activity);
    when(mappingService.mapEntityToDto(any(Activity.class))).thenReturn(activityDto);

    ActivityDto createdActivity = activityService.create(activityDto, user);

    assertNotNull(createdActivity);
    assertEquals(activityDto.getTitle(), createdActivity.getTitle());
    verify(activityRepository, times(1)).save(any(Activity.class));
  }

  @Test
  void testCreateActivityAlreadyExists() {
    when(activityRepository.findByTitle(activityDto.getTitle())).thenReturn(Optional.of(activity));

    assertThrows(ActivityCreationException.class, () -> activityService.create(activityDto, user));
  }

  @Test
  void testGetAllActivities() {
    when(activityRepository.findAll()).thenReturn(List.of(activity));
    when(mappingService.mapEntityToDto(activity)).thenReturn(activityDto);

    List<ActivityDto> activities = activityService.getAllActivities();

    assertFalse(activities.isEmpty());
    assertEquals(1, activities.size());
    assertEquals(activityDto.getTitle(), activities.get(0).getTitle());
  }

  @Test
  void testGetActivityById() {
    when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
    when(mappingService.mapEntityToDto(activity)).thenReturn(activityDto);

    ActivityDto result = activityService.getActivityById(1L);

    assertNotNull(result);
    assertEquals(activityDto.getTitle(), result.getTitle());
  }

  @Test
  void testGetActivityById_NotFound() {
    when(activityRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ActivityNotFoundException.class, () -> activityService.getActivityById(1L));
  }

  @Test
  void testUpdateActivity() {
    when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
    when(activityRepository.save(activity)).thenReturn(activity);
    when(mappingService.mapEntityToDto(activity)).thenReturn(activityDto);

    ActivityDto updatedActivity = activityService.update(1L, activityDto);

    assertNotNull(updatedActivity);
    assertEquals(activityDto.getTitle(), updatedActivity.getTitle());
    verify(activityRepository, times(1)).save(activity);
  }

  @Test
  void testDeleteActivity() {
    when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
    doNothing().when(activityRepository).delete(activity);

    activityService.deleteActivity(1L);

    verify(activityRepository, times(1)).delete(activity);
  }

//  @Test
//  void testAddUserToActivity() {
//    when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
//    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
//    when(activityRepository.save(activity)).thenReturn(activity);
//    when(mappingService.mapEntityToDto(activity)).thenReturn(activityDto);
//
//    ActivityDto updatedActivity = activityService.addUserToActivity(1L, "testUser");
//
//    assertNotNull(updatedActivity);
//    verify(activityRepository, times(1)).save(activity);
//  }

  @Test
  void testAddUserToActivity_UserNotFound() {
    when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
    when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> activityService.addUserToActivity(1L, "testUser"));
  }

//  @Test
//  void testRemoveUserFromActivity() {
//    when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
//    when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
//    activity.addUser(user);
//    doNothing().when(activityRepository).save(activity);
//
//    activityService.removeUserFromActivity(1L, "testUser");
//
//    verify(activityRepository, times(1)).save(activity);
//  }
}

