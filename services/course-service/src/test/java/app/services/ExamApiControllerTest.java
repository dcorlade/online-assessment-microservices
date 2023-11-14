package app.services;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.communication.Authorisation;
import app.constants.Constants;
import app.controllers.ExamApiController;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.models.ExamQuestion;
import app.models.Question;
import app.models.StudentAnswer;
import app.models.StudentExam;
import app.models.Topic;
import app.repositories.QuestionRepository;
import app.repositories.TopicRepository;
import app.serializerfactory.Serializer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ExamApiController.class)
class ExamApiControllerTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient TopicRepository topicRepository;
    @MockBean
    private transient QuestionRepository questionRepository;

    private transient List<Topic> topics;
    private transient StudentExam studentExam;

    @BeforeEach
    void setup() {
        topics = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            Question q = new Question();
            q.setId(i);
            Answer a = new Answer();
            a.setId(1);
            q.setAnswers(List.of(a));
            List<Question> questionsList = new ArrayList<>();
            questionsList.add(q);
            Topic t = new Topic();
            t.setQuestions(questionsList);
            topics.add(t);
        }
        List<ExamQuestion> examQuestions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ExamQuestion eq = new ExamQuestion();
            eq.setQuestion(i);
            StudentAnswer sa = new StudentAnswer();
            sa.setAnswer(1);
            sa.setSelected(false);
            eq.setStudentAnswers(List.of(sa));
            examQuestions.add(eq);
        }
        studentExam = new StudentExam();
        studentExam.setExamQuestions(examQuestions);
    }

    @Test
    void nonAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);
            mockMvc.perform(post(Constants.POST_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(Constants.NOT_AUTHORIZED_STRING)));
        }
    }

    @Test
    void moreThanTenTopics() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            topics.add(new Topic());
            when(topicRepository.findByCourseId(1))
                .thenReturn(topics);
            mockMvc.perform(post(Constants.POST_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409))
                .andExpect(content().string(containsString(Constants.TOO_MANY_TOPICS)));
        }
    }

    @Test
    void someTopicHasNoQuestions() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            topics.get(1).getQuestions().clear();
            when(topicRepository.findByCourseId(1))
                .thenReturn(topics);
            mockMvc.perform(post(Constants.POST_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409))
                .andExpect(content().string(containsString(Constants.TOPIC_WITHOUT_Q)));
        }
    }

    @Test
    void notEnoughQuestions() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            topics.remove(0);
            when(topicRepository.findByCourseId(1))
                .thenReturn(topics);
            mockMvc.perform(post(Constants.POST_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(containsString(Constants.NOT_ENOUGH_Q)));
        }
    }

    //
    @Test
    void examQuestionsByCourseIdTest() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            Question q = new Question();
            q.setId(10);
            Answer a = new Answer();
            a.setId(1);
            q.setAnswers(List.of(a));
            topics.remove(0);
            topics.get(0).getQuestions().add(q);
            JSONObject expected = new JSONObject();
            expected.put("studentExam", serializer.serialize(studentExam));
            when(topicRepository.findByCourseId(1))
                .thenReturn(topics);
            mockMvc.perform(post(Constants.POST_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(expected.toString())));
        }
    }

    @Test
    void questionsToStudentExamTest() {
        Question q = new Question();
        q.setId(10);
        Answer a = new Answer();
        a.setId(1);
        q.setAnswers(List.of(a));
        topics.remove(0);
        topics.get(0).getQuestions().add(q);
        List<Question> questionList = topics.stream()
            .flatMap(x -> x.getQuestions().stream())
            .collect(Collectors.toList());
        ExamApiController controller = new ExamApiController(null, null);
        StudentExam actual = controller.questionsToStudentExam(questionList);
        Assertions.assertEquals(actual, studentExam);
    }

    @Test
    void getQuestionsByIdTest() throws Exception {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ids.add(i);
        }
        List<Question> questionList = topics.stream()
            .flatMap(x -> x.getQuestions().stream())
            .collect(Collectors.toList());
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            when(questionRepository.findAllById(ids))
                .thenReturn(questionList);
            mockMvc.perform(post("/course_service/getQuestionsById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.QUESTION_IDS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(Constants.QUESTION_STRING)));
        }
    }
}