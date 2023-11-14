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

class ExamTest {

    private transient Exam exam;
    private transient StudentExam studentExam1;
    private transient List<StudentExam> studentExams;
    private transient JSONObject jsonObject;
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setup() {
        studentExam1 = new StudentExam();
        studentExam1.setId(1);
        studentExam1.setUser("1");
        studentExam1.setExamId(1);
        studentExam1.setStartingTime(new Timestamp(1));
        studentExams = new ArrayList<>();
        studentExams.add(studentExam1);

        exam = new Exam();
        exam.setId(1);
        exam.setCourseId(1);
        exam.setStart(new Timestamp(0));
        exam.setEnd(new Timestamp(1));
        exam.setStudentExamList(studentExams);

        jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("courseId", 1);
        jsonObject.put("start", new Timestamp(0).toString());
        jsonObject.put("end", new Timestamp(1).toString());
        JSONArray studentExamListJsonArray = new JSONArray();
        for (StudentExam studentExam : studentExams) {
            studentExamListJsonArray.put(serializer.serialize(studentExam));
        }
        jsonObject.put("studentExamList", studentExamListJsonArray);
    }

    @Test
    void constructorToJson() {
        Assertions.assertEquals(exam, serializer.deserialize(jsonObject.toString(), Exam.class));
    }

    @Test
    void toJson() {
        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(exam));
    }

    @Test
    void testToString() {
        String s = "Exam{"
            + "id=" + 1
            + ", courseId=" + 1
            + ", start=" + new Timestamp(0).toString()
            + ", end=" + new Timestamp(1).toString()
            + ", studentExamList=" + studentExams.toString()
            + "}";

        Assertions.assertEquals(s, exam.toString());
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        Exam temp = (Exam) serializer.deserialize(jsonObject.toString(), Exam.class);
        Assertions.assertEquals(new Exam(), temp);

        Assertions.assertEquals("{}", serializer.serialize(temp));
    }
}