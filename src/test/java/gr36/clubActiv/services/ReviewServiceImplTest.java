package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.Review;
import gr36.clubActiv.exeption_handling.exeptions.ReviewNotFounException;
import gr36.clubActiv.repository.ReviewRepository;
import gr36.clubActiv.services.interfaces.ReviewService;
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

class ReviewServiceImplTest {

  @Mock
  private ReviewRepository reviewRepository;

  @InjectMocks
  private ReviewServiceImpl reviewService;

  private Review review;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    review = new Review();
    review.setId(1L);
    review.setTitle("Great Product");
    review.setDescription("This product exceeded my expectations.");
    review.setRating(5);
    review.setCreatedBy("testUser");
    review.setCreatedAt(LocalDateTime.now());
  }

  @Test
  void testSaveReview() {
    when(reviewRepository.save(review)).thenReturn(review);

    Review savedReview = reviewService.saveReview(review);

    assertNotNull(savedReview);
    assertEquals("Great Product", savedReview.getTitle());
    assertEquals("testUser", savedReview.getCreatedBy());
    assertEquals(5, savedReview.getRating());
    verify(reviewRepository, times(1)).save(review);
  }

  @Test
  void testDeleteReview() {
    doNothing().when(reviewRepository).deleteById(1L);

    reviewService.deleteReview(1L);

    verify(reviewRepository, times(1)).deleteById(1L);
  }

  @Test
  void testGetAllReviews() {
    when(reviewRepository.findAll()).thenReturn(List.of(review));

    List<Review> reviews = reviewService.getAllReviews();

    assertFalse(reviews.isEmpty());
    assertEquals(1, reviews.size());
    assertEquals("Great Product", reviews.get(0).getTitle());
    verify(reviewRepository, times(1)).findAll();
  }

  @Test
  void testFindById_Success() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

    Optional<Review> foundReview = reviewService.findById(1L);

    assertTrue(foundReview.isPresent());
    assertEquals("Great Product", foundReview.get().getTitle());
    verify(reviewRepository, times(1)).findById(1L);
  }

  @Test
  void testFindById_NotFound() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Review> foundReview = reviewService.findById(1L);

    assertFalse(foundReview.isPresent());
  }

  @Test
  void testUpdateReview_Success() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
    when(reviewRepository.saveAndFlush(review)).thenReturn(review);

    reviewService.update(1L);

    verify(reviewRepository, times(1)).saveAndFlush(review);
  }

  @Test
  void testUpdateReview_NotFound() {
    when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ReviewNotFounException.class, () -> reviewService.update(1L));
  }
}
