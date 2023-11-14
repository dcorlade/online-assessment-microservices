package app.models;

import java.sql.Timestamp;
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
@Table(name = "student_exam")
public class StudentExam {

    private static final transient String gradeStr = "grade";
    private static final transient String nullStr = "null";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "correct_questions")
    private Integer correctQuestions;

    @Column(name = gradeStr)
    private Float grade;

    @Column(name = "extra_time")
    private Integer extraTime;

    @Column(name = "user")
    private String user;

    @Column(name = "exam")
    private Integer examId;

    @Column(name = "starting_time")
    private Timestamp startingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam", insertable = false, updatable = false)
    private Exam exam;

    @OneToMany(mappedBy = "studentExam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ExamQuestion> examQuestions;


    /**
     * Construct a String representation of the StudentExam object.
     *
     * @return String representation of the StudentExam object.
     */
    @Override
    public String toString() {
        return "StudentExam{"
            + "id=" + Objects.toString(id, nullStr)
            + ", correctQuestions=" + Objects.toString(correctQuestions, nullStr)
            + ", grade=" + Objects.toString(grade, nullStr)
            + ", extraTime=" + Objects.toString(extraTime, nullStr)
            + ", userId=" + Objects.toString(user, nullStr)
            + ", examId=" + Objects.toString(examId, nullStr)
            + ", startingTime=" + Objects.toString(startingTime, nullStr)
            + ", examQuestions=" + Objects.toString(examQuestions, nullStr)
            + '}';
    }
}