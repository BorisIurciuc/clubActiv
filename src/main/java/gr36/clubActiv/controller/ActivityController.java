package gr36.clubActiv.controller;

import gr36.clubActiv.domain.dto.ActivityDto;
import gr36.clubActiv.domain.entity.User;
import gr36.clubActiv.exeption_handling.exeptions.UserNotFoundException;
import gr36.clubActiv.services.interfaces.ActivityService;
import gr36.clubActiv.services.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

  private final ActivityService service;
  private final UserService userService;
  private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

  public ActivityController(ActivityService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<ActivityDto> create(@RequestBody ActivityDto activityDto, Authentication authentication) {
    log.info("Creating new activity: {}", activityDto);
    String username = authentication.getName();
    User author = userService.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    ActivityDto createdActivity = service.create(activityDto, author);
    log.info("Activity created successfully with ID: {}", createdActivity.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdActivity);
  }

  @GetMapping
  public List<ActivityDto> getAllActivities() {
    log.info("Fetching all activities");
    return service.getAllActivities();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ActivityDto> getActivityById(@PathVariable Long id) {
    log.info("Fetching activity by ID: {}", id);
    ActivityDto activity = service.getActivityById(id);
    return ResponseEntity.ok(activity);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ActivityDto dto, Authentication authentication) {
    String currentUsername = authentication.getName();
    ActivityDto activity = service.getActivityById(id);
    User currentUser = userService.findByUsername(currentUsername)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    boolean isAuthor = activity.getAuthorId() != null && activity.getAuthorId().equals(currentUser.getId());

    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    if (!isAdmin && !isAuthor) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this activity");
    }

    ActivityDto updatedActivity = service.update(id, dto);
    return ResponseEntity.ok(updatedActivity);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
    String currentUsername = authentication.getName();
    ActivityDto activity = service.getActivityById(id);
    User currentUser = userService.findByUsername(currentUsername)
        .orElseThrow(() -> new RuntimeException("User not found"));

    boolean isAuthor = activity.getAuthorId() != null && activity.getAuthorId().equals(currentUser.getId());
    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    if (!isAdmin && !isAuthor) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this activity");
    }

    service.deleteActivity(id);
    log.info("Activity with ID {} deleted successfully.", id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{activity_id}/add-user")

  public ResponseEntity<ActivityDto> addUserToActivity(@PathVariable Long activity_id, Authentication authentication) {
    String username = authentication.getName();
    User user = userService.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    ActivityDto updatedActivity = service.addUserToActivity(activity_id, username);
    return ResponseEntity.ok(updatedActivity);
  }

  @GetMapping("/my-activities")
  public ResponseEntity<List<ActivityDto>> getMyActivities(Authentication authentication) {
    String username = authentication.getName();
    User user = userService.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

    List<ActivityDto> activities;
    if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      activities = service.getAllActivities();
    } else {
      activities = service.getActivitiesByUserId(user.getId());
    }

    return ResponseEntity.ok(activities);
  }

  @DeleteMapping("/{activity_id}/remove-user")
  public ResponseEntity<?> removeUserFromActivity(@PathVariable Long activity_id, Authentication authentication) {
    String currentUsername = authentication.getName();
    User currentUser = userService.findByUsername(currentUsername)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    ActivityDto activity = service.getActivityById(activity_id);

    boolean isAuthor = activity.getAuthorId() != null && activity.getAuthorId().equals(currentUser.getId());

    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

    log.info("Checking authorization for user: {}, isAdmin: {}, isAuthor: {}", currentUsername, isAdmin, isAuthor);

    if (!isAdmin && !isAuthor) {
      log.warn("User {} is not authorized to remove users from activity {}", currentUsername, activity_id);
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to remove users from this activity.");
    }

    ActivityDto updatedActivity = service.removeUserFromActivity(activity_id, currentUser.getUsername());
    return ResponseEntity.ok(updatedActivity);
  }

}
