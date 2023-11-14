package app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import app.json.JsonSerializerFactory;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnswerTest {

    transient Answer answer;
    transient String description = "description";
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setUp() {
        answer = new Answer(1, null, 2, 3, description, true);
    }

    @Test
    void testFromJsonConstructor() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("questionId", 2);
        jsonObject.put("order", 3);
        jsonObject.put("description", description);
        jsonObject.put("correct", true);

        Answer actualAnswer = (Answer) serializer.deserialize(jsonObject.toString(), Answer.class);

        assertEquals(answer, actualAnswer);
    }

    @Test
    void testToString() {
        String actual = answer.toString();
        String expected = "Answer{id=" + 1
            + ", questionId=" + 2
            + ", order=" + 3
            + ", description=" + description
            + ", correct=" + true
            + "}";

        assertEquals(expected, actual);
    }

    @Test
    void toJson() {
        JSONObject answerJson = new JSONObject();
        answerJson.put("id", 1);
        answerJson.put("questionId", 2);
        answerJson.put("order", 3);
        answerJson.put("description", description);
        answerJson.put("correct", true);

        String expected = serializer.serialize(answer);
        String actual = answerJson.toString();

        assertEquals(expected, actual);
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        Answer temp = (Answer) serializer.deserialize(jsonObject.toString(), Answer.class);
        Assertions.assertEquals(new Answer(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }


}