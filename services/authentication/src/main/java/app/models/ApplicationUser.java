package app.models;

import app.constants.Constants;
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
import org.json.JSONObject;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class ApplicationUser {

    @Id
    @Column(name = "net_id")
    private String netId;

    @Column(name = Constants.PASSWORD_STRING)
    private String password;

    @Column(name = Constants.ROLE_STRING)
    private Integer role;

    @Column(name = "extra_time")
    private Integer extraTime;

    /**
     * Construct User object with a JSON string.
     *
     * @param json JSONObject with all needed attributes to construct a User object.
     */
    public ApplicationUser(JSONObject json) {
        if (json.has("netId")) {
            this.netId = (String) json.get("netId");
        }
        if (json.has(Constants.PASSWORD_STRING)) {
            this.password = (String) json.get(Constants.PASSWORD_STRING);
        }
        if (json.has(Constants.ROLE_STRING)) {
            this.role = (Integer) json.get(Constants.ROLE_STRING);
        }
        if (json.has("extraTime")) {
            this.extraTime = (Integer) json.get("extraTime");
        }
    }

    /**
     * Construct a Json representation of the User object.
     *
     * @return Json String representation of the User object.
     */
    public String toJson() {
        JSONObject json = new JSONObject();
        if (netId != null) {
            json.put("netId", netId);
        }
        if (role != null) {
            json.put(Constants.ROLE_STRING, role);
        }
        if (password != null) {
            json.put(Constants.PASSWORD_STRING, password);
        }
        if (extraTime != null) {
            json.put("extraTime", extraTime);
        }
        return json.toString();
    }

    /**
     * Construct a String representation of the User object.
     *
     * @return String representation of the User object.
     */
    public String toString() {
        return "Course{"
            + "netId=" + Objects.toString(netId, Constants.NULL_STRING)
            + ", password=" + Objects.toString(password, Constants.NULL_STRING)
            + ", role=" + Objects.toString(role, Constants.NULL_STRING)
            + ", extraTime=" + Objects.toString(extraTime, Constants.NULL_STRING)
            + "}";
    }
}