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
@Table(name = "answers")
public class Answer {
    private static final transient String orderStr = "order";
    private static final transient String descriptionStr = "description";
    private static final transient String correctStr = "correct";
    private static final transient String nullStr = "null";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "question", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Column(name = "question")
    private Integer questionId;

    @Column(name = orderStr)
    private Integer order;

    @Column(name = descriptionStr)
    private String description;

    @Column(name = correctStr)
    private Boolean correct;

    /**
     * Construct a String representation of the Answer object.
     *
     * @return a String-representation of the object,
     *     containing all values of the object attributes.
     */
    public String toString() {
        return "Answer{id=" + Objects.toString(id, nullStr)
            + ", questionId=" + Objects.toString(questionId, nullStr)
            + ", order=" + Objects.toString(order, nullStr)
            + ", description=" + Objects.toString(description, nullStr)
            + ", correct=" + Objects.toString(correct, nullStr)
            + "}";
    }
}