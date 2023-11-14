package app.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Exam;
import app.models.StudentExam;
import app.repositories.ExamRepository;
import app.repositories.StudentExamRepository;
import app.serializerfactory.Serializer;
import java.util.ArrayList;
import java.util.List;
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
@WebMvcTest(ExamController.class)
class ExamControllerTest {

    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient ExamRepository examRepository;
    @MockBean
    private transient StudentExamRepository studentExamRepository;
    private transient Exam exam;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();


    @BeforeEach
    void setup() {
        exam = new Exam();
        exam.setId(1);

        StudentExam studentExam1 = new StudentExam();
        StudentExam studentExam2 = new StudentExam();
        StudentExam studentExam3 = new StudentExam();
        studentExam1.setGrade(1.0f);
        studentExam2.setGrade(10.0f);
        studentExam3.setGrade(0.0f);
        List<StudentExam> studentExams = new ArrayList<>();
        studentExams.add(studentExam1);
        studentExams.add(studentExam2);
        studentExams.add(studentExam3);

        doReturn(exam).when(examRepository).findById(1);
        doReturn(exam).when(examRepository).save(exam);
        doNothing().when(examRepository).deleteById(5);
        doReturn(studentExams).when(studentExamRepository).findByExamId(5);
    }

    @Test
    void examById() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/examById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(serializer.serialize(exam))));
        }
    }

    @Test
    void examByIdNoEntity() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/examById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 0}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(containsString(Constants.NOT_FOUND)));
        }
    }

    @Test
    void examByIdNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/examById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }


    @Test
    void updateExam() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/updateExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(exam))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(serializer.serialize(exam))));
        }
    }

    @Test
    void updateExamNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/updateExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(exam))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void deleteExam() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/deleteExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("Success")));
        }
    }

    @Test
    void deleteExamNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/deleteExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void getAverageGrade() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/getAverageGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"examId\": 5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("{\"avgGrade\":5.5}")));
        }
    }

    @Test
    void getAverageGradeNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/getAverageGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"examId\": 5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void getAmountOfStudentsNoAuth() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);
            mockMvc.perform(post(Constants.GET_AMOUNT_OF_STUDENTS_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{\"examId\":2}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void getAmountOfStudentsSuccessOne() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            Exam exam2 = new Exam();
            exam2.setId(2);
            StudentExam studentExamTim = new StudentExam();
            studentExamTim.setUser("Tim");
            List<StudentExam> list = new ArrayList<>();
            list.add(studentExamTim);
            list.add(studentExamTim);
            list.add(studentExamTim);
            exam2.setStudentExamList(list);

            doReturn(exam2).when(examRepository).findById(2);
            doReturn(list).when(studentExamRepository).findByExamId(2);

            mockMvc.perform(post(Constants.GET_AMOUNT_OF_STUDENTS_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{\"examId\":2}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("{\"amount\":1}")));
        }
    }

    @Test
    void getAmountOfStudentsSuccessMultiple() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            Exam exam2 = new Exam();
            exam2.setId(2);
            StudentExam studentExam1 = new StudentExam();
            StudentExam studentExam2 = new StudentExam();
            StudentExam studentExam3 = new StudentExam();
            studentExam1.setUser("1");
            studentExam2.setUser("2");
            studentExam3.setUser("3");
            List<StudentExam> list = new ArrayList<>();
            list.add(studentExam1);
            list.add(studentExam2);
            list.add(studentExam3);
            exam2.setStudentExamList(list);

            doReturn(exam2).when(examRepository).findById(2);
            doReturn(list).when(studentExamRepository).findByExamId(2);

            mockMvc.perform(post(Constants.GET_AMOUNT_OF_STUDENTS_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{\"examId\":2}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString("{\"amount\":3}")));
        }
    }

    @Test
    void getAmountOfStudentsNonExistingExamId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);

            doReturn(null).when(examRepository).findById(3);

            mockMvc.perform(post(Constants.GET_AMOUNT_OF_STUDENTS_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{\"examId\":3}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(containsString("There exists no exam with that id")));
        }
    }
}