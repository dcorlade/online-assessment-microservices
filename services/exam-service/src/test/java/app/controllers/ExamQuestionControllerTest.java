package app.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.ExamQuestion;
import app.repositories.ExamQuestionRepository;
import app.serializerfactory.Serializer;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExamQuestionController.class)
class ExamQuestionControllerTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient ExamQuestionRepository examQuestionRepository;
    private transient ExamQuestion examQuestion;
    private transient List<ExamQuestion> examQuestions;
    private transient JSONObject examQuestionsJson;

    @BeforeEach
    void setup() {

        examQuestion = new ExamQuestion();
        examQuestion.setId(5);

        ExamQuestion examQuestion1 = new ExamQuestion();
        ExamQuestion examQuestion2 = new ExamQuestion();
        examQuestion1.setId(1);
        examQuestion1.setQuestion(1);
        examQuestion2.setId(2);
        examQuestion2.setQuestion(2);

        examQuestions = new ArrayList<>();
        examQuestions.add(examQuestion1);
        examQuestions.add(examQuestion2);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(serializer.serialize(examQuestion1));
        jsonArray.put(serializer.serialize(examQuestion2));
        examQuestionsJson = new JSONObject();
        examQuestionsJson.put("examQuestionList", jsonArray);

        doReturn(examQuestion).when(examQuestionRepository).findById(5);
        doReturn(examQuestion).when(examQuestionRepository).save(examQuestion);
        doNothing().when(examQuestionRepository).deleteById(5);
        doReturn(examQuestions).when(examQuestionRepository).findExamQuestionsByStudentExamId(10);
    }


    @Test
    void examQuestionById() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/examQuestionById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(serializer.serialize(examQuestion)));
        }
    }

    @Test
    void examQuestionByIdNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/examQuestionById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void examQuestionByIdNoEntity() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/examQuestionById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": -1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(Constants.NOT_FOUND));
        }
    }

    @Test
    void updateExamQuestion() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/updateExamQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(examQuestion))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(serializer.serialize(examQuestion)));
        }
    }

    @Test
    void updateExamQuestionNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/updateExamQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(examQuestion))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void deleteExamQuestion() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/deleteExamQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(examQuestion))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string("Success"));
        }
    }

    @Test
    void deleteExamQuestionNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/deleteExamQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(examQuestion))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void examQuestionsByStudentExamIdAndUserId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/examQuestionsByStudentExamId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 10}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(examQuestionsJson.toString()));
        }
    }

    @Test
    void examQuestionsByStudentExamIdAndUserIdNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/examQuestionsByStudentExamId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 10}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }
}