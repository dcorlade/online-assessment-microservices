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
@Table(name = "course")
public class Course {
    private static final transient String nameStr = "name";
    private static final transient String yearStr = "year";
    private static final transient String nullStr = "null";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = nameStr)
    private String name;

    @Column(name = yearStr)
    private String year;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Topic> topicList;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enrollment> enrollmentList;

    /**
     * Construct a String representation of the Course object.
     *
     * @return a String-representation of the object,
     *     containing all values of the object attributes.
     */
    public String toString() {
        return "Course{"
            + "id=" + Objects.toString(id, nullStr)
            + ", courseCode=" + Objects.toString(courseCode, nullStr)
            + ", name=" + Objects.toString(name, nullStr)
            + ", year=" + Objects.toString(year, nullStr)
            + ", topicList=" + Objects.toString(topicList, nullStr)
            + ", enrollmentList" + Objects.toString(enrollmentList, nullStr)
            + "}";
    }
}
