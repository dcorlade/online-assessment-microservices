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
@Table(name = "topic")
public class Topic {

    private static final transient String nameStr = "name";
    private static final transient String nullStr = "null";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = nameStr)
    private String name;

    @JoinColumn(name = "course", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @Column(name = "course")
    private Integer courseId;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions;


    /**
     * Construct a String representation of the Topic object.
     *
     * @return String representation of the Topic object.
     */
    @Override
    public String toString() {
        return "Topic{"
            + "id=" + Objects.toString(id, nullStr)
            + ", name='" + Objects.toString(name, nullStr)
            + ", courseId=" + Objects.toString(courseId, nullStr)
            + ", questions=" + Objects.toString(questions, nullStr)
            + '}';
    }
}
