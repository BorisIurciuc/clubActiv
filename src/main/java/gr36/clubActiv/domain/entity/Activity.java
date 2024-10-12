package gr36.clubActiv.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "activities")
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "address")
  private String address;

  @Column(name = "date-time")
  private LocalDate startDate;

  @Column(name = "image")
  private String image;

  @Column(name = "description")
  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user-activities",
      joinColumns = @JoinColumn(name = "activity_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @JsonIgnore
  private List<User> users;
  public void addUser(User user) {
    if (!users.contains(user)) {
      users.add(user);
    }
  }


  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id")
  private User author;

  // Getters and Setters
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  // New Getter and Setter for Author
  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Activity activity)) {
      return false;
    }
    return Objects.equals(getId(), activity.getId()) &&
        Objects.equals(getTitle(), activity.getTitle()) &&
        Objects.equals(getAddress(), activity.getAddress()) &&
        Objects.equals(getStartDate(), activity.getStartDate()) &&
        Objects.equals(getImage(), activity.getImage()) &&
        Objects.equals(getDescription(), activity.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getAddress(), getStartDate(), getImage(), getDescription());
  }

  @Override
  public String toString() {
    return String.format("Activity: id - %d, title - %s, address - %s, startDate - %s, image - %s, description - %s, author - %s",
        id, title, address, startDate, image, description, author.getUsername());
  }
}
