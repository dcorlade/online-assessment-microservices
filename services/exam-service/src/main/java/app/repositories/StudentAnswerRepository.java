package app.repositories;

import app.models.StudentAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {
    StudentAnswer findById(int id);

    List<StudentAnswer> findByExamQuestion(int examQuestionId);

}
