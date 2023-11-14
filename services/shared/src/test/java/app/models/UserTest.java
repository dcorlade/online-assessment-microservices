package app.models;

import app.json.JsonSerializerFactory;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private transient JSONObject jsonObject;
    private transient User user;
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setup() {
        user = new User();
        user.setNetId("1");
        user.setPassword("hello");
        user.setRole(2);
        user.setExtraTime(1);

        jsonObject = new JSONObject();
        jsonObject.put("netId", "1");
        jsonObject.put("password", "hello");
        jsonObject.put("role", 2);
        jsonObject.put("extraTime", 1);
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals(user, serializer.deserialize(jsonObject.toString(), User.class));
    }

    @Test
    void testToString() {
        String s = "Course{"
            + "netId=" + "1"
            + ", password=" + "hello"
            + ", role=" + 2
            + ", extraTime=" + 1
            + "}";
        Assertions.assertEquals(s, user.toString());
    }

    @Test
    void testToJson() {
        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(user));
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        User temp = (User) serializer.deserialize(jsonObject.toString(), User.class);
        Assertions.assertEquals(new User(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }

}
