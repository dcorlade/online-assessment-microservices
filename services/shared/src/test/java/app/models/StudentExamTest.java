package app.models;

import app.json.JsonSerializerFactory;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentExamTest {


    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();
    private transient StudentExam studentExam;
    private transient List<ExamQuestion> examQuestions;
    private transient JSONObject jsonObject;
    private transient ExamQuestion examQuestion;
    private transient List<StudentAnswer> studentAnswerList;
    private transient StudentAnswer studentAnswer;

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
        examQuestions = new ArrayList<>();
        examQuestions.add(examQuestion);

        studentExam = new StudentExam();
        studentExam.setId(1);
        studentExam.setCorrectQuestions(1);
        studentExam.setGrade(1.0f);
        studentExam.setExtraTime(1);
        studentExam.setUser("1");
        studentExam.setExamId(1);
        studentExam.setStartingTime(new Timestamp(0));
        studentExam.setExamQuestions(examQuestions);

        jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("correctQuestions", 1);
        jsonObject.put("grade", 1.0f);
        jsonObject.put("extraTime", 1);
        jsonObject.put("userId", "1");
        jsonObject.put("examId", 1);
        jsonObject.put("startingTime", new Timestamp(0).toString());

        JSONArray a = new JSONArray();
        for (ExamQuestion question : examQuestions) {
            a.put(serializer.serialize(question));
        }

        jsonObject.put("examQuestions", a);
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals(studentExam,
            serializer.deserialize(jsonObject.toString(), StudentExam.class));
    }

    @Test
    void testToString() {
        String s = "StudentExam{"
            + "id=" + 1
            + ", correctQuestions=" + 1
            + ", grade=" + 1.0f
            + ", extraTime=" + 1
            + ", userId=" + "1"
            + ", examId=" + 1
            + ", startingTime=" + new Timestamp(0).toString()
            + ", examQuestions=" + examQuestions.toString()
            + '}';
        Assertions.assertEquals(s, studentExam.toString());
    }

    @Test
    void testToJson() {
        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(studentExam));
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        StudentExam temp =
            (StudentExam) serializer.deserialize(jsonObject.toString(), StudentExam.class);
        Assertions.assertEquals(new StudentExam(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }
}
