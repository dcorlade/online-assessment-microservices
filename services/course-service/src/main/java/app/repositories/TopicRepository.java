package app.repositories;

import app.models.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findByCourseId(int courseId);
}