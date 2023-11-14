package app.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Exam;
import app.models.StudentAnswer;
import app.models.StudentExam;
import app.repositories.StudentAnswerRepository;
import app.repositories.StudentExamRepository;
import app.serializerfactory.Serializer;
import java.sql.Timestamp;
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
@WebMvcTest(StudentAnswerController.class)
class StudentAnswerControllerTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient StudentAnswerRepository studentAnswerRepository;
    @MockBean
    private transient StudentExamRepository studentExamRepository;
    private transient StudentAnswer studentAnswer1;
    private transient StudentAnswer studentAnswer2;
    private transient JSONObject studentAnswerListJson;
    private transient StudentExam studentExam;

    @BeforeEach
    void setup() {
        studentAnswer1 = new StudentAnswer();
        studentAnswer1.setId(0);
        studentAnswer1.setExamQuestionId(1);
        studentAnswer1.setAnswer(2);
        studentAnswer1.setSelected(true);

        studentAnswer2 = new StudentAnswer();
        studentAnswer2.setId(1);
        studentAnswer2.setExamQuestionId(2);
        studentAnswer2.setAnswer(3);
        studentAnswer2.setSelected(false);

        StudentAnswer studentAnswer3 = new StudentAnswer();
        studentAnswer3.setExamQuestionId(2);
        studentAnswer3.setAnswer(1);
        studentAnswer3.setSelected(true);

        List<StudentAnswer> studentAnswerList = new ArrayList<>();
        studentAnswerList.add(studentAnswer1);
        studentAnswerList.add(studentAnswer2);

        JSONArray jsonArray = new JSONArray();
        for (StudentAnswer s : studentAnswerList) {
            jsonArray.put(serializer.serialize(s));
        }
        studentAnswerListJson = new JSONObject();
        studentAnswerListJson.put("studentAnswerList", jsonArray);

        Exam exam = new Exam();
        exam.setId(0);

        studentExam = new StudentExam();
        studentExam.setId(42);
        studentExam.setExamId(1);
        studentExam.setExam(exam);
        studentExam.setExtraTime(5);

        doReturn(studentAnswer1).when(studentAnswerRepository).findById(0);
        doReturn(studentAnswerList).when(studentAnswerRepository).findByExamQuestion(5);
        doNothing().when(studentAnswerRepository).deleteById(0);
        doReturn(studentAnswer2).when(studentAnswerRepository).save(studentAnswer2);
        doReturn(studentAnswer1).when(studentAnswerRepository).save(studentAnswer3);
        doReturn(studentExam).when(studentExamRepository).findById(42);
    }

    @Test
    void studentAnswerById() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/studentAnswerById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 0}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(serializer.serialize(studentAnswer1))));
        }
    }

    @Test
    void studentAnswerByIdNoEntity() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/studentAnswerById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(containsString(
                    "Entity not found")));
        }
    }

    @Test
    void notAuthorizedExceptionStudentAnswerById() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/studentAnswerById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 0}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(
                    Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void studentAnswerByExamQuestionId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/studentAnswerByExamQuestionId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"examQuestionId\": 5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(studentAnswerListJson.toString())));
        }
    }

    @Test
    void notAuthorizedExceptionStudentAnswerByExamQuestionId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/studentAnswerByExamQuestionId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.EXAM_QUESTION_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(
                    Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void deleteStudentAnswer() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/deleteStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 0}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(
                    "Success")));
        }
    }

    @Test
    void notAuthorizedExceptionDeleteStudentAnswer() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/deleteStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.EXAM_QUESTION_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(
                    Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void updateStudentAnswerTest() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/createStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{\"examQuestionId\":2,\"answer\":3,\"id\":1,\"selected\":false}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(serializer.serialize(studentAnswer2))));
        }
    }

    @Test
    void notAuthorizedExceptionUpdateStudentAnswer() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/createStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.EXAM_QUESTION_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(
                    Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void updateStudentAnswer() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 10000L);
        studentExam.setStartingTime(timestamp);
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            when(studentAnswerRepository.save(any(StudentAnswer.class))).thenReturn(studentAnswer1);
            mockMvc.perform(post("/exam_service/updateStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(
                    "{\"studentExamId\":42,\"id\":0,\"answerId\":1,\"examQuestionId\":2,\"selected\":true}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(serializer.serialize(studentAnswer1))));
        }
    }

    @Test
    void updateStudentAnswerAfterEnd() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() - 1000000000L);
        studentExam.setStartingTime(timestamp);
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/updateStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(
                    "{\"studentExamId\":42,\"answerId\":1,\"examQuestionId\":2,\"selected\":true}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString("The exam is over.")));
        }
    }

    @Test
    void notAuthorizedExceptionUpdateStudentExam() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/updateStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.EXAM_QUESTION_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(
                    Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }
}