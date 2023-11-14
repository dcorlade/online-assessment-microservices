package app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import app.communication.Authorisation;
import app.json.JsonSerializerFactory;
import app.models.Course;
import app.models.Question;
import app.models.Topic;
import app.repositories.TopicRepository;
import app.serializerfactory.Serializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TopicService.class)
public class TopicServiceTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient TopicRepository topicRepository;

    private transient TopicService topicService;
    private transient Course course1;
    private transient Topic topic1;
    private transient Topic topicNotYetAdded;
    private transient Question question1;
    private transient List<Topic> allTopics;
    private transient Integer nonExistingTopicId = 10;
    private transient Integer nonExistingCourseId = 10;

    @BeforeEach
    void setup() {
        topicService = new TopicService(topicRepository);

        question1 = new Question();
        question1.setId(1);
        question1.setTopicId(1);
        question1.setTitle("Question 1");

        topic1 = new Topic();
        topic1.setId(1);
        topic1.setName("Topic 1");
        topic1.setCourseId(1);
        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        topic1.setQuestions(questions);

        topicNotYetAdded = new Topic();
        topicNotYetAdded.setId(2);
        topicNotYetAdded.setName("Topic 2");
        topicNotYetAdded.setCourseId(1);
        List<Question> empty = new ArrayList<>();
        topicNotYetAdded.setQuestions(empty);

        allTopics = new ArrayList<>();
        allTopics.add(topic1);

        course1 = new Course();
        course1.setId(1);
        course1.setCourseCode("c1");
        course1.setName("course1");
        List<Topic> topics = new ArrayList<>();
        topics.add(topic1);
        course1.setTopicList(topics);
        course1.setYear("2020");

        doReturn(Optional.of(topic1)).when(topicRepository).findById(1);
        doReturn(allTopics).when(topicRepository).findAll();
        doReturn(topic1).when(topicRepository).save(topic1);
        doReturn(topicNotYetAdded).when(topicRepository).save(topicNotYetAdded);
    }

    @Test
    void getTopicTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.getTopic(topic1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(serializer.serialize(topic1), res.getBody());
        }
    }

    @Test
    void getTopicTestFailure() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.getTopic(nonExistingTopicId, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getTopicTestNotAuthorized() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res = topicService.getTopic(nonExistingTopicId, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void getTopicsByCourseTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.getTopicsByCourse(course1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(
                new JSONObject().put("topics",
                    new JSONArray().put(serializer.serialize(topic1))).toString(),
                res.getBody());
        }
    }

    @Test
    void getTopicsByCourseTestFailure() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.getTopicsByCourse(nonExistingCourseId, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getTopicsByCourseTestNotAuthorized() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res = topicService.getTopicsByCourse(nonExistingTopicId, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void addTopicTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.addTopic(topicNotYetAdded, "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void addTopicTestNotAuthorized() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = topicService.addTopic(topic1, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void updateTopicTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.updateTopic(topic1, "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void updateTopicTestFailure() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.updateTopic(topicNotYetAdded, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void updateTopicTestNotAuthorized() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = topicService.updateTopic(topic1, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void deleteTopicTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.deleteTopic(topic1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void deleteTopicTestFailure() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = topicService.deleteTopic(nonExistingTopicId, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void deleteTopicTestNotAuthorized() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = topicService.deleteTopic(nonExistingTopicId, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }
}
