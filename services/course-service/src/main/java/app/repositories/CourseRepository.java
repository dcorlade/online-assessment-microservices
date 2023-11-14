package app.repositories;

import app.models.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByCourseCode(String courseCode);
}