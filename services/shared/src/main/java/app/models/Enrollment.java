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
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user")
    private String userId;

    @Column(name = "course")
    private Integer courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course", insertable = false, updatable = false)
    private Course course;

    /**
     * Construct a String representation of the Enrollment object.
     *
     * @return a String-representation of the object,
     *     containing all values of the object attributes,
     *     except for the course attribute.
     */
    @Override
    public String toString() {
        return "Enrollment{"
            + "id=" + Objects.toString(id, "null")
            + ", userId='" + Objects.toString(userId, "null") + '\''
            + ", courseId=" + Objects.toString(courseId, "null")
            + '}';
    }
}