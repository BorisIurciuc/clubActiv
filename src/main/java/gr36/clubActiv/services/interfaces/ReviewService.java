package gr36.clubActiv.services.interfaces;

import gr36.clubActiv.domain.entity.Review;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface ReviewService {

  Review saveReview(Review review);

  void deleteReview(Long reviewId);

  List<Review> getAllReviews();

  Optional<Review> findById(Long id);

  void update(Long id);
}
