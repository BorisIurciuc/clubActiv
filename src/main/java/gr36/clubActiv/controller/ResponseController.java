package gr36.clubActiv.controller;

import gr36.clubActiv.domain.entity.Response;
import gr36.clubActiv.services.interfaces.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
