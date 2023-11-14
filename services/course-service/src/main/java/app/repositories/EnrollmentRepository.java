package app.repositories;

import app.models.Enrollment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findEnrollmentByCourseIdAndUserId(Integer courseId, String userId);
}