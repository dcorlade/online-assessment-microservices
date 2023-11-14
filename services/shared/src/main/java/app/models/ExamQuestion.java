package app.models;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "exam_questions")
public class ExamQuestion {

    private static final transient String questionStr = "question";
    private static final transient String correctStr = "correct";
    private static final transient String nullStr = "null";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = questionStr)
    private Integer question;

    @Column(name = "student_exam")
    private Integer studentExamId;

    @Column(name = correctStr)
    private Boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_exam", insertable = false, updatable = false)
    private ExamQuestion studentExam;

    @OneToMany(mappedBy = "examQuestion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StudentAnswer> studentAnswers;


    /**
     * Construct a String representation of the ExamQuestion object.
     *
     * @return a String-representation of the object,
     *     containing all values of the object attributes.
     */
    @Override
    public String toString() {
        return "ExamQuestion{"
            + "id=" + Objects.toString(id, nullStr)
            + ", question=" + Objects.toString(question, nullStr)
            + ", correct=" + Objects.toString(correct, nullStr)
            + ", studentExamId=" + Objects.toString(studentExamId, nullStr)
            + ", studentAnswers=" + Objects.toString(studentAnswers, nullStr)
            + '}';
    }
}