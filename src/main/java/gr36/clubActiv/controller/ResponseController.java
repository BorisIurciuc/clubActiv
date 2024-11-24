package gr36.clubActiv.controller;

import gr36.clubActiv.domain.entity.Response;
import gr36.clubActiv.services.interfaces.ResponseService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

  private final ResponseService responseService;

  public ResponseController(ResponseService responseService) {
    this.responseService = responseService;
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @PostMapping("/review/{reviewId}")
  public ResponseEntity<Response> addResponse(@PathVariable Long reviewId,
      @RequestBody Response response,
      Authentication authentication) {
    response.setCreatedBy(authentication.getName());
    Response savedResponse = responseService.addResponse(reviewId, response);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedResponse);
  }

  @GetMapping("/review/{reviewId}")
  public ResponseEntity<List<Response>> getResponsesByReview(@PathVariable Long reviewId) {
    List<Response> responses = responseService.getResponsesByReviewId(reviewId);
    return ResponseEntity.ok(responses);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteResponse(@PathVariable Long id, Authentication authentication) {
    Response responseToBeDeleted = responseService.findResponseById(id);
    if (responseToBeDeleted == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Response not found");    }


    boolean isCreator = responseToBeDeleted.getCreatedBy().equals(authentication.getName());
    boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

    if (!isCreator && !isAdmin) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body("You are not authorized to delete this response.");
    }

    responseService.deleteResponse(id);
    return ResponseEntity.noContent().build();
  }
}
