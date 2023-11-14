package app.models;

import app.json.JsonSerializerFactory;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentAnswerTest {

    private transient StudentAnswer studentAnswer;
    private transient JSONObject jsonObject;
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setup() {
        studentAnswer = new StudentAnswer();
        studentAnswer.setId(1);
        studentAnswer.setAnswer(2);
        studentAnswer.setExamQuestionId(1);
        studentAnswer.setSelected(true);

        jsonObject = new JSONObject();
        jsonObject.put("id", 1)
            .put("examQuestionId", 1)
            .put("answer", 2)
            .put("selected", true);
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals(studentAnswer,
            serializer.deserialize(jsonObject.toString(), StudentAnswer.class));
    }

    @Test
    void testToJson() {
        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(studentAnswer));
    }

    @Test
    void testToString() {
        String s = "StudentAnswer{"
            + "id=" + 1
            + ", examQuestionId=" + 1
            + ", answer=" + 2
            + ", selected=" + true
            + '}';
        Assertions.assertEquals(s, studentAnswer.toString());
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        StudentAnswer temp =
            (StudentAnswer) serializer.deserialize(jsonObject.toString(), StudentAnswer.class);
        Assertions.assertEquals(new StudentAnswer(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }
}
