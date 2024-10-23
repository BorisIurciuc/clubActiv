package gr36.clubActiv.repository;

import gr36.clubActiv.domain.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
  List<Response> findByReviewId(Long reviewId);
}