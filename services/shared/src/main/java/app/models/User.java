package app.models;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "user")
public class User {

    private static final transient String passwordStr = "password";
    private static final transient String roleStr = "role";
    private static final transient String nullStr = "null";
    @Id
    @Column(name = "net_id")
    private String netId;

    @Column(name = passwordStr)
    private String password;

    @Column(name = roleStr)
    private Integer role;

    @Column(name = "extra_time")
    private Integer extraTime;

    /**
     * Construct a String representation of the User object.
     *
     * @return String representation of the User object.
     */
    public String toString() {
        return "Course{"
            + "netId=" + Objects.toString(netId, nullStr)
            + ", password=" + Objects.toString(password, nullStr)
            + ", role=" + Objects.toString(role, nullStr)
            + ", extraTime=" + Objects.toString(extraTime, nullStr)
            + "}";
    }
}