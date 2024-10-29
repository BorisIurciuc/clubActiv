package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.Response;
import gr36.clubActiv.domain.entity.Review;
import gr36.clubActiv.exeption_handling.exeptions.ResponseNotFoundException;
import gr36.clubActiv.exeption_handling.exeptions.ReviewNotFounException;
import gr36.clubActiv.repository.ResponseRepository;
import gr36.clubActiv.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ResponseServiceImplTest {

  @Mock
  private ResponseRepository responseRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @InjectMocks
  private ResponseServiceImpl responseService;

  private Response response;
  private Review review;

  @BeforeEach
  void setUp() {
    openMocks(this);

    review = new Review();
    review.setId(1L);
    review.setTitle("Great Product");
    review.setDescription("This product exceeded my expectations.");
    review.setCreatedBy("testUser");

    response = new Response();
    response.setId(1L);
    response.setContent("I agree with this review!");
    response.setCreatedBy("responseUser");
    response.setCreatedAt(LocalDateTime.now());
    response.setReview(review);
  }

  @Test
  void testAddResponse_Success() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
    when(responseRepository.save(response)).thenReturn(response);

    Response savedResponse = responseService.addResponse(1L, response);

    assertNotNull(savedResponse);
    assertEquals("I agree with this review!", savedResponse.getContent());
    assertEquals("responseUser", savedResponse.getCreatedBy());
    verify(responseRepository, times(1)).save(response);
  }

  @Test
  void testAddResponse_ReviewNotFound() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ReviewNotFounException.class, () -> responseService.addResponse(1L, response));
  }

  @Test
  void testDeleteResponse() {
    doNothing().when(responseRepository).deleteById(1L);

    responseService.deleteResponse(1L);

    verify(responseRepository, times(1)).deleteById(1L);
  }

  @Test
  void testGetResponsesByReviewId_Success() {
    when(reviewRepository.existsById(1L)).thenReturn(true);
    when(responseRepository.findByReviewId(1L)).thenReturn(List.of(response));

    List<Response> responses = responseService.getResponsesByReviewId(1L);

    assertFalse(responses.isEmpty());
    assertEquals(1, responses.size());
    assertEquals("I agree with this review!", responses.get(0).getContent());
    verify(responseRepository, times(1)).findByReviewId(1L);
  }

  @Test
  void testGetResponsesByReviewId_ReviewNotFound() {
    when(reviewRepository.existsById(1L)).thenReturn(false);

    assertThrows(ReviewNotFounException.class, () -> responseService.getResponsesByReviewId(1L));
  }

  @Test
  void testFindResponseById_Success() {
    when(responseRepository.findById(1L)).thenReturn(Optional.of(response));

    Response foundResponse = responseService.findResponseById(1L);

    assertNotNull(foundResponse);
    assertEquals("I agree with this review!", foundResponse.getContent());
    verify(responseRepository, times(1)).findById(1L);
  }

  @Test
  void testFindResponseById_NotFound() {
    when(responseRepository.findById(1L)).thenReturn(Optional.empty());

    Response foundResponse = responseService.findResponseById(1L);

    assertNull(foundResponse);
  }
}
