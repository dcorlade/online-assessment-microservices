package app.models;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_answers")
public class StudentAnswer {

    private static final transient String answerStr = "answer";
    private static final transient String selectedStr = "selected";
    private static final transient String nullStr = "null";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "exam_question")
    private Integer examQuestionId;

    @Column(name = answerStr)
    private Integer answer;

    @Column(name = selectedStr)
    private Boolean selected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_question", insertable = false, updatable = false)
    private ExamQuestion examQuestion;


    /**
     * Construct a String representation of the StudentAnswer object.
     *
     * @return String representation of the StudentAnswer object.
     */
    @Override
    public String toString() {
        return "StudentAnswer{"
            + "id=" + Objects.toString(id, nullStr)
            + ", examQuestionId=" + Objects.toString(examQuestionId, nullStr)
            + ", answer=" + Objects.toString(answer, nullStr)
            + ", selected=" + Objects.toString(selected, nullStr)
            + '}';
    }
}