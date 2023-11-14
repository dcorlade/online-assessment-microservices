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

class ExamQuestionTest {

    private transient ExamQuestion examQuestion;
    private transient StudentAnswer studentAnswer;
    private transient List<StudentAnswer> studentAnswerList;
    private transient JSONObject jsonObject;
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setup() {
        studentAnswer = new StudentAnswer();
        studentAnswer.setSelected(true);

        studentAnswerList = new ArrayList<>();
        studentAnswerList.add(studentAnswer);

        examQuestion = new ExamQuestion();
        examQuestion.setId(1);
        examQuestion.setQuestion(1);
        examQuestion.setStudentExamId(1);
        examQuestion.setCorrect(true);
        examQuestion.setStudentAnswers(studentAnswerList);

        jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("studentExamId", 1);
        jsonObject.put("question", 1);
        jsonObject.put("correct", true);

        JSONArray a = new JSONArray();
        for (StudentAnswer s : studentAnswerList) {
            a.put(serializer.serialize(s));
        }
        jsonObject.put("studentAnswers", a);
    }

    @Test
    void constructorToJson() {
        Assertions.assertEquals(examQuestion,
            serializer.deserialize(jsonObject.toString(), ExamQuestion.class));
    }

    @Test
    void toJson() {
        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(examQuestion));
    }

    @Test
    void testToString() {
        String s = "ExamQuestion{"
            + "id=" + 1
            + ", question=" + 1
            + ", correct=" + true
            + ", studentExamId=" + 1
            + ", studentAnswers=" + studentAnswerList.toString()
            + '}';
        Assertions.assertEquals(s, examQuestion.toString());
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        ExamQuestion temp =
            (ExamQuestion) serializer.deserialize(jsonObject.toString(), ExamQuestion.class);
        Assertions.assertEquals(new ExamQuestion(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }
}