package app.models;

import app.json.JsonSerializerFactory;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

    private transient Enrollment enrollment;
    private transient JSONObject jsonObject;
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setup() {
        enrollment = new Enrollment(1, "userName", 1, null);
        jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("userId", "userName");
        jsonObject.put("courseId", 1);

    }

    @Test
    void constructorFromJson() {
        Assertions.assertEquals(enrollment,
            serializer.deserialize(jsonObject.toString(), Enrollment.class));
    }

    @Test
    void toJson() {
        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(enrollment));
    }

    @Test
    void testToString() {
        String s = "Enrollment{"
            + "id=" + 1
            + ", userId=" + "'userName'"
            + ", courseId=" + 1
            + '}';

        Assertions.assertEquals(s, enrollment.toString());
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        Enrollment temp =
            (Enrollment) serializer.deserialize(jsonObject.toString(), Enrollment.class);
        Assertions.assertEquals(new Enrollment(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }
}