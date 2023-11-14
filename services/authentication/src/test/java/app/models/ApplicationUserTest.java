package app.models;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {
    private transient JSONObject jsonObject;
    private transient ApplicationUser user;

    @BeforeEach
    void setup() {
        user = new ApplicationUser();
        user.setNetId("1");
        user.setPassword("hello");
        user.setRole(1);
        user.setExtraTime(1);

        jsonObject = new JSONObject();
        jsonObject.put("netId", "1");
        jsonObject.put("password", "hello");
        jsonObject.put("role", 1);
        jsonObject.put("extraTime", 1);
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals(user, new ApplicationUser(jsonObject));
    }

    @Test
    void testToString() {
        String s = "Course{"
            + "netId=" + "1"
            + ", password=" + "hello"
            + ", role=" + 1
            + ", extraTime=" + 1
            + "}";
        Assertions.assertEquals(s, user.toString());
    }

    @Test
    void testToJson() {
        Assertions.assertEquals(jsonObject.toString(), user.toJson());
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        ApplicationUser temp = new ApplicationUser(jsonObject);
        Assertions.assertEquals(new ApplicationUser(), temp);

        Assertions.assertEquals("{}", temp.toJson());
    }
}