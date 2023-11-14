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

class CourseTest {

    private transient Course course;
    private transient Enrollment enrollment;
    private transient Topic topic;
    private transient List<Topic> topicList;
    private transient List<Enrollment> enrollmentList;
    private transient String twenty = "2020";
    private transient String courseCode = "courseCode";
    private transient String courseName = "courseName";
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    @BeforeEach
    void setup() {
        enrollment = new Enrollment(1, "userName", 1, null);
        topic = new Topic(1, "topicName", course, 2, null);

        topicList = new ArrayList<>();
        enrollmentList = new ArrayList<>();
        topicList.add(topic);
        enrollmentList.add(enrollment);

        course = new Course(1, courseCode, courseName, twenty, topicList, enrollmentList);
    }

    @Test
    void constructorFromJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put(courseCode, courseCode);
        jsonObject.put("name", courseName);
        jsonObject.put("year", twenty);
        JSONArray topicListJsonArray = new JSONArray();
        for (Topic topic : topicList) {
            topicListJsonArray.put(serializer.serialize(topic));
        }
        jsonObject.put("topicList", topicListJsonArray);
        JSONArray enrollmentListJsonArray = new JSONArray();
        for (Enrollment enrollment : enrollmentList) {
            enrollmentListJsonArray.put(serializer.serialize(enrollment));
        }
        jsonObject.put("enrollmentList", enrollmentListJsonArray);


        Assertions
            .assertEquals(course, serializer.deserialize(jsonObject.toString(), Course.class));
    }


    @Test
    void toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put(courseCode, courseCode);
        jsonObject.put("name", courseName);
        jsonObject.put("year", twenty);
        JSONArray topicListJsonArray = new JSONArray();
        for (Topic topic : topicList) {
            topicListJsonArray.put(serializer.serialize(topic));
        }
        jsonObject.put("topicList", topicListJsonArray);
        JSONArray enrollmentListJsonArray = new JSONArray();
        for (Enrollment enrollment : enrollmentList) {
            enrollmentListJsonArray.put(serializer.serialize(enrollment));
        }
        jsonObject.put("enrollmentList", enrollmentListJsonArray);

        Assertions.assertEquals(jsonObject.toString(), serializer.serialize(course));
    }


    @Test
    void testToString() {
        String expectedString = "Course{"
            + "id=" + 1
            + ", courseCode=" + courseCode
            + ", name=" + courseName
            + ", year=" + twenty
            + ", topicList=" + topicList.toString()
            + ", enrollmentList" + enrollmentList.toString()
            + "}";

        Assertions.assertEquals(expectedString, course.toString());
    }

    @Test
    void nullTest() {
        JSONObject jsonObject = new JSONObject("{}");
        Course course = (Course) serializer.deserialize(jsonObject.toString(), Course.class);
        Assertions.assertEquals(new Course(), course);

        Assertions.assertEquals("{}", serializer.serialize(course));
    }
}