package app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.models.Question;
import app.models.Topic;
import app.repositories.QuestionRepository;
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
@WebMvcTest(QuestionService.class)
public class QuestionServiceTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient QuestionRepository questionRepository;
    private transient QuestionService questionService;
    private transient Topic topic1;
    private transient Question question1;
    private transient Question questionNotYetAdded;
    private transient Answer q1a1;
    private transient Answer q1a2;
    private transient Answer q1a3;
    private transient Answer q1a4;
    private transient List<Question> allQuestions; // excludes questionNotYetAdded

    @BeforeEach
    void setup() {
        questionService = new QuestionService(questionRepository);

        q1a1 = new Answer();
        q1a2 = new Answer();
        q1a3 = new Answer();
        q1a4 = new Answer();
        q1a1.setId(1);
        q1a1.setQuestionId(1);
        q1a1.setCorrect(true);
        q1a1.setOrder(1);
        q1a2.setId(2);
        q1a2.setQuestionId(1);
        q1a2.setCorrect(false);
        q1a2.setOrder(2);
        q1a3.setId(3);
        q1a3.setQuestionId(1);
        q1a3.setCorrect(false);
        q1a3.setOrder(3);
        q1a4.setId(4);
        q1a4.setQuestionId(1);
        q1a4.setCorrect(false);
        q1a4.setOrder(4);


        question1 = new Question();
        question1.setId(1);
        question1.setTopicId(1);
        question1.setTitle("Question 1");
        question1.setDescription("This is question 1");
        List<Answer> answers = new ArrayList<>();
        question1.setAnswers(answers);

        allQuestions = new ArrayList<>();
        allQuestions.add(question1);

        questionNotYetAdded = new Question();
        questionNotYetAdded.setId(2);
        questionNotYetAdded.setTopicId(2);
        questionNotYetAdded.setTitle("Question 2");
        questionNotYetAdded.setDescription("This is question 2");
        questionNotYetAdded.setAnswers(answers);

        topic1 = new Topic();
        topic1.setId(1);
        topic1.setCourseId(1);
        topic1.setName("Topic 1");
        List<Question> questions = new ArrayList<Question>();
        questions.add(question1);
        topic1.setQuestions(questions);

        doReturn(Optional.of(question1)).when(questionRepository).findById(question1.getId());
        doReturn(allQuestions).when(questionRepository).findAll();
        doReturn(question1).when(questionRepository).save(question1);
        doReturn(questionNotYetAdded).when(questionRepository).save(questionNotYetAdded);
    }

    @Test
    void getQuestionTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = questionService.getQuestion(question1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(serializer.serialize(question1), res.getBody());
        }
    }

    @Test
    void getQuestionTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res =
                questionService.getQuestion(Constants.NON_EXISTING_QUESTION_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void getQuestionsByTopicTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = questionService.getQuestionsByTopic(topic1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(new JSONObject()
                .put("questions", new JSONArray().put(serializer.serialize(question1)))
                .toString(), res.getBody());
        }
    }

    @Test
    void getQuestionsByTopicTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                questionService.getQuestionsByTopic(Constants.NON_EXISTING_TOPIC_ID, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getQuestionsByTopicTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res =
                questionService.getQuestionsByTopic(Constants.NON_EXISTING_QUESTION_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void getRandomQuestionsByTopicTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                questionService.getRandomQuestionsByTopic(topic1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(serializer.serialize(question1), res.getBody());
        }
    }

    @Test
    void getRandomQuestionsByTopicTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                questionService.getRandomQuestionsByTopic(Constants.NON_EXISTING_TOPIC_ID, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getRandomQuestionsByTopicTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res =
                questionService.getRandomQuestionsByTopic(Constants.NON_EXISTING_TOPIC_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void addQuestionTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = questionService.addQuestion(questionNotYetAdded, "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }


    @Test
    void addQuestionTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = questionService.addQuestion(question1, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void updateQuestionTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = questionService.updateQuestion(question1, "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void updateQuestionTestFailure() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = questionService.updateQuestion(questionNotYetAdded, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void updateQuestionTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = questionService.updateQuestion(question1, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void deleteQuestionTestSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = questionService.deleteQuestion(question1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void deleteQuestionTestFailure() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res =
                questionService.deleteQuestion(Constants.NON_EXISTING_QUESTION_ID, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void deleteQuestionTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res =
                questionService.deleteQuestion(Constants.NON_EXISTING_QUESTION_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }
}
