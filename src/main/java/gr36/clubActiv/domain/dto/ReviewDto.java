package gr36.clubActiv.domain.dto;

import gr36.clubActiv.domain.entity.Review;
import java.time.LocalDateTime;

public class ReviewDto {

  private Long id;
  private String title;
  private String description;
  private int rating;
  private String createdBy;
  private LocalDateTime createdAt;

  // Default constructor
  public ReviewDto() {
  }

  // Constructor with all fields
  public ReviewDto(Long id, String title, String description, int rating, String createdBy, LocalDateTime createdAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.rating = rating;
    this.createdBy = createdBy;
    this.createdAt = createdAt;
  }


  public ReviewDto(Review review) {
    this.id = review.getId();
    this.title = review.getTitle();
    this.description = review.getDescription();
    this.rating = review.getRating();
    this.createdBy = review.getCreatedBy();
    this.createdAt = review.getCreatedAt();
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "ReviewDto{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", rating=" + rating +
        ", createdBy='" + createdBy + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
