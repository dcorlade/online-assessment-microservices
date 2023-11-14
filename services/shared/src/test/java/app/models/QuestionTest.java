package app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import app.json.JsonSerializerFactory;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    transient Answer answer;
    private transient Question question;
    private transient JSONObject jsonObject;
    private transient List<Answer> answerList;
    private transient String hello = "hello";
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setup() {
        answer = new Answer(1, null, 2, 3, hello, true);
        answerList = new ArrayList<>();
        answerList.add(answer);
        question = new Question();
        question.setId(1);
        question.setTopicId(2);
        question.setTitle("Title1");
        question.setDescription(hello);
        question.setAnswers(answerList);

        jsonObject = new JSONObject();
        jsonObject.put("id", 1)
            .put("title", "Title1")
            .put("topicId", 2)
            .put("description", hello);
        JSONArray jsonArray = new JSONArray();
        for (Answer answer : answerList) {
            jsonArray.put(serializer.serialize(answer));
        }
        jsonObject.put("answers", jsonArray);
    }


    @Test
    void testConstructor() {
        assertEquals(question, serializer.deserialize(jsonObject.toString(), Question.class));
    }

    @Test
    void testToString() {
        String expected = "Question{"
            + "id=" + 1
            + ", topicId=" + 2
            + ", title=" + "Title1"
            + ", description=" + hello
            + ", answers=" + answerList.toString()
            + "}";
        assertEquals(expected, question.toString());
    }

    @Test
    void testToJson() {
        assertEquals(jsonObject.toString(), serializer.serialize(question));
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        Question temp = (Question) serializer.deserialize(jsonObject.toString(), Question.class);
        Assertions.assertEquals(new Question(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }
}
