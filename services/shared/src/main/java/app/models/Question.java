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
@Table(name = "questions")
public class Question {

    private static final transient String titleStr = "title";
    private static final transient String descriptionStr = "description";
    private static final transient String nullStr = "null";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "topic", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;

    @Column(name = "topic")
    private Integer topicId;

    @Column(name = titleStr)
    private String title;

    @Column(name = descriptionStr)
    private String description;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Answer> answers;

    /**
     * Construct a String representation of the Question object.
     *
     * @return String representation of the Question object.
     */
    public String toString() {
        return "Question{"
            + "id=" + Objects.toString(id, nullStr)
            + ", topicId=" + Objects.toString(topicId, nullStr)
            + ", title=" + Objects.toString(title, nullStr)
            + ", description=" + Objects.toString(description, nullStr)
            + ", answers=" + Objects.toString(answers, nullStr)
            + "}";
    }
}

