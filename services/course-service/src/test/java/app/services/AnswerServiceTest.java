package app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.models.Question;
import app.repositories.AnswerRepository;
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
@WebMvcTest(AnswerService.class)
public class AnswerServiceTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient AnswerRepository answerRepository;

    private transient AnswerService answerService;
    private transient List<Answer> answerListQuestion1;
    private transient List<Answer> answerListAll;
    private transient Answer answer1;
    private transient Answer answerNotYetAdded;
    private transient Question question1;


    @BeforeEach
    void setup() {
        answerService = new AnswerService(answerRepository);

        answerListQuestion1 = new ArrayList<>();
        answerListAll = new ArrayList<>();

        answer1 = new Answer();
        answer1.setId(1);
        answer1.setQuestionId(1);
        answer1.setCorrect(true);
        answer1.setDescription("This is answer 1");
        answer1.setOrder(1);
        answerListQuestion1.add(answer1);
        answerListAll.add(answer1);

        answerNotYetAdded = new Answer();
        answerNotYetAdded.setId(2);
        answerNotYetAdded.setDescription("Some answer");
        answerNotYetAdded.setOrder(2);
        answerNotYetAdded.setCorrect(false);
        answerNotYetAdded.setQuestionId(1);

        question1 = new Question();
        question1.setId(1);
        question1.setDescription("This is question 1");
        question1.setTitle("Question 1");
        question1.setTopicId(1);
        question1.setAnswers(answerListQuestion1);

        Optional<Answer> optional1 = Optional.of(answer1);
        doReturn(optional1).when(answerRepository).findById(1);
        doReturn(answerListAll).when(answerRepository).findAll();
        doNothing().when(answerRepository).deleteById(1);
        doReturn(answerNotYetAdded).when(answerRepository).save(answerNotYetAdded);
    }

    @Test
    void getAnswerTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = answerService.getAnswer(answer1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(serializer.serialize(answer1), res.getBody());
        }
    }

    @Test
    void getAnswerTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                answerService.getAnswer(Constants.NON_EXISTING_ANSWER_ID, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getAnswerTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res =
                answerService.getAnswer(Constants.NON_EXISTING_ANSWER_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
            assertEquals(Constants.NOT_AUTHORIZED_STRING, res.getBody());
        }
    }

    @Test
    void getAnswersByQuestionTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res = answerService.getAnswersByQuestion(answer1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(
                new JSONObject().put("answers", new JSONArray().put(serializer.serialize(answer1)))
                    .toString(),
                res.getBody());
            // Might still change if i add more answers to question 1
        }
    }

    @Test
    void getAnswersByQuestionTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                answerService.getAnswersByQuestion(Constants.NON_EXISTING_QUESTION_ID, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
            // Might still change if i add more answers to question 1
        }
    }

    @Test
    void getAnswersByQuestionTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res =
                answerService.getAnswersByQuestion(Constants.NON_EXISTING_ANSWER_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
            assertEquals(Constants.NOT_AUTHORIZED_STRING, res.getBody());
        }
    }

    @Test
    void addAnswerTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);


            ResponseEntity<String> res = answerService.addAnswer(answerNotYetAdded, "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }


    @Test
    void addAnswerTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = answerService.addAnswer(answer1, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
            assertEquals(Constants.NOT_AUTHORIZED_STRING, res.getBody());
        }
    }

    @Test
    void updateAnswerTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = answerService.updateAnswer(answer1, "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void updateAnswerTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = answerService.updateAnswer(answerNotYetAdded, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void updateAnswerTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = answerService.updateAnswer(answer1, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
            assertEquals(Constants.NOT_AUTHORIZED_STRING, res.getBody());
        }
    }

    @Test
    void deleteAnswerTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res = answerService.deleteAnswer(answer1.getId(), "");

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void deleteAnswerTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            ResponseEntity<String> res =
                answerService.deleteAnswer(Constants.NON_EXISTING_ANSWER_ID, "");

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void deleteAnswerTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = answerService.deleteAnswer(answer1.getId(), "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
            assertEquals(Constants.NOT_AUTHORIZED_STRING, res.getBody());
        }
    }
}
