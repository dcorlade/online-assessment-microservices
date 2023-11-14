package app.repositories;

import app.models.StudentExam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentExamRepository extends JpaRepository<StudentExam, Integer> {
    StudentExam findById(int id);

    List<StudentExam> findByExamAndUser(int examId, String userId);

    List<StudentExam> findByUser(String netId);

    List<StudentExam> findByExamId(int examId);

}
