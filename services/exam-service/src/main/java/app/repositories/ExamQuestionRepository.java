package app.repositories;

import app.models.ExamQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Integer> {
    ExamQuestion findById(int id);

    List<ExamQuestion> findExamQuestionsByStudentExamId(int studentExamId);
}
