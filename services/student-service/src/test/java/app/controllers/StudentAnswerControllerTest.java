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
@WebMvcTest(StudentAnswerController.class)
class StudentAnswerControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void getStudentAnswerNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentAnswerById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/getStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.STUDENT_ANSWER_NOT_FOUND)));
        }
    }

    @Test
    void getStudentAnswerSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentAnswerById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/getStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void getStudentAnswersByExamQuestionIdNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentAnswerByExamQuestionId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/getStudentAnswersByExamQuestionId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Exam Question ID not found")));
        }
    }

    @Test
    void getStudentAnswersByExamQuestionIdSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentAnswerByExamQuestionId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/getStudentAnswersByExamQuestionId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void updateStudentAnswerNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/updateStudentAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/updateStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.STUDENT_ANSWER_NOT_FOUND)));
        }
    }

    @Test
    void updateStudentAnswerSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/updateStudentAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/updateStudentAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

}