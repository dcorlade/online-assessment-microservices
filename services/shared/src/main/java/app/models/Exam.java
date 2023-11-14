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
@Table(name = "exam")
public class Exam {
    private static final transient String startStr = "start";
    private static final transient String endStr = "end";
    private static final transient String nullStr = "null";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "course")
    private Integer courseId;

    @Column(name = startStr)
    private Timestamp start;

    @Column(name = endStr)
    private Timestamp end;

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StudentExam> studentExamList;

    /**
     * Construct a String representation of the Exam object.
     *
     * @return a String-representation of the object,
     *     containing all values of the object attributes.
     */
    public String toString() {
        return "Exam{"
            + "id=" + Objects.toString(id, nullStr)
            + ", courseId=" + Objects.toString(courseId, nullStr)
            + ", start=" + Objects.toString(start, nullStr)
            + ", end=" + Objects.toString(end, nullStr)
            + ", studentExamList=" + Objects.toString(studentExamList, nullStr)
            + "}";
    }
}
