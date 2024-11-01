package gr36.clubActiv.services;

import gr36.clubActiv.domain.entity.Response;
import gr36.clubActiv.domain.entity.Review;
import gr36.clubActiv.exeption_handling.exeptions.ReviewNotFounException;
import gr36.clubActiv.repository.ResponseRepository;
import gr36.clubActiv.repository.ReviewRepository;
import gr36.clubActiv.services.interfaces.ReviewService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final ResponseRepository responseRepository;

  public ReviewServiceImpl(ReviewRepository reviewRepository, ResponseRepository responseRepository) {
    this.reviewRepository = reviewRepository;
    this.responseRepository = responseRepository;
  }

  @Override
  public Review saveReview(Review review) {
    return reviewRepository.save(review);
  }


  @Override
  public void deleteReview(Long reviewId) {
    reviewRepository.deleteById(reviewId);
  }

  @Override
  public List<Review> getAllReviews() {
    return reviewRepository.findAll();
  }

  @Override
  public Optional<Review> findById(Long id) {
    return reviewRepository.findById(id);
  }

  @Override
  public void update(Long id, Review updatedReview) {
    Review existingReview = reviewRepository.findById(id)
        .orElseThrow(() -> new ReviewNotFounException(id));

    existingReview.setTitle(updatedReview.getTitle());
    existingReview.setDescription(updatedReview.getDescription());
    existingReview.setRating(updatedReview.getRating());

    if (updatedReview.getCreatedBy() == null) {
      existingReview.setCreatedBy(existingReview.getCreatedBy());
    } else {
      existingReview.setCreatedBy(updatedReview.getCreatedBy());
    }

    reviewRepository.save(existingReview);
  }

  @Override
  public void deleteReviewsByUser(String username) {
    List<Review> userReviews = reviewRepository.findByCreatedBy(username);

    userReviews.forEach(review -> {
      List<Response> responses = responseRepository.findByReviewId(review.getId());
      responseRepository.deleteAll(responses);
      reviewRepository.delete(review);
    });
  }





}



