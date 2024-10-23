package gr36.clubActiv.repository;

import gr36.clubActiv.domain.entity.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

   Optional<Activity> findByTitle(String title);
   List<Activity> findByUsersId(Long user_id);


  List<Activity> findByAuthorId(Long authorId);
}
