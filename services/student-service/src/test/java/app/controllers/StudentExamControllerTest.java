package app.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.communication.StudentServiceCommunication;
import app.constants.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentExamController.class)
class StudentExamControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void studentExamByIdNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentExamById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/studentExamById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.STUDENT_EXAM_NOT_FOUND)));
        }
    }

    @Test
    void studentExamByIdSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentExamById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/studentExamById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void studentExamByUserIdNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentExamByUserId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/studentExamByUserId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Student exams not found")));
        }
    }

    @Test
    void studentExamByUserIdSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentExamByUserId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/studentExamByUserId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void createStudentExamNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/createStudentExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/createStudentExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Exam creation unsuccessful")));
        }
    }

    @Test
    void createStudentExamSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/createStudentExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/createStudentExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void submitStudentExamNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/submitStudentExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/submitStudentExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Submitting exam unsuccessful")));
        }
    }

    @Test
    void submitStudentExamSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/submitStudentExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/submitStudentExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }
}