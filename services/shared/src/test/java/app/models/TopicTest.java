package app.models;

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

class TopicTest {

    private transient JSONObject jsonObject;
    private transient Topic topic;
    private transient List<Answer> answerList;
    private transient Answer answer;
    private transient Question question;
    private transient List<Question> questionList;
    private final transient String hello = "hello";
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
        questionList = new ArrayList<>();
        questionList.add(question);

        topic = new Topic();
        topic.setId(1);
        topic.setName(hello);
        topic.setCourseId(1);
        topic.setQuestions(questionList);

        JSONArray a = new JSONArray();
        for (Question q : questionList) {
            a.put(serializer.serialize(q));
        }

        jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("name", hello);
        jsonObject.put("courseId", 1);
        jsonObject.put("questions", a);
    }


    @Test
    void testConstructor() {
        Assertions.assertEquals(topic, serializer.deserialize(jsonObject.toString(), Topic.class));
    }


    @Test
    void testToString() {
        String s = "Topic{"
            + "id=" + 1
            + ", name='" + hello
            + ", courseId=" + 1
            + ", questions=" + questionList.toString()
            + '}';
        Assertions.assertEquals(s, topic.toString());
    }

    @Test
    void testToJson() {
        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(topic));
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        Topic temp = (Topic) serializer.deserialize(jsonObject.toString(), Topic.class);
        Assertions.assertEquals(new Topic(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }

}
