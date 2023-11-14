package app.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.communication.TeacherServiceCommunication;
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
@WebMvcTest(ExamController.class)
class ExamControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void examByIdNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/examById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/examById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.ENTITY_NOT_FOUND)));
        }
    }

    @Test
    void examByIdSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/examById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_FINAL);

            mockMvc.perform(post("/teacher_service/examById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_FINAL)));
        }
    }

    @Test
    void createExamNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                Constants.UPDATE_EXAM,
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/createExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to create")));
        }
    }

    @Test
    void createExamSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                Constants.UPDATE_EXAM,
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_FINAL);

            mockMvc.perform(post("/teacher_service/createExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_FINAL)));
        }
    }

    @Test
    void updateExamNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/updateExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/updateExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to update")));
        }
    }

    @Test
    void updateExamSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/updateExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_FINAL);

            mockMvc.perform(post("/teacher_service/updateExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_FINAL)));
        }
    }

    @Test
    void deleteExamNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/deleteExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/deleteExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.ENTITY_NOT_FOUND)));
        }
    }

    @Test
    void deleteExamSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/deleteExam",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_FINAL);

            mockMvc.perform(post("/teacher_service/deleteExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString("Success")));
        }
    }

    @Test
    void studentExamsByExamIdNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentExamsByExamId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/studentExamsByExamId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Exam not found")));
        }
    }

    @Test
    void studentExamsByExamIdSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/studentExamsByExamId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_FINAL);

            mockMvc.perform(post("/teacher_service/studentExamsByExamId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_FINAL)));
        }
    }

    @Test
    void getAmountOfStudentsNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/getAmountOfStudents",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/getAmountOfStudents")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Exam not found")));
        }
    }

    @Test
    void getAmountOfStudentsSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/getAmountOfStudents",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_FINAL);

            mockMvc.perform(post("/teacher_service/getAmountOfStudents")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_FINAL)));
        }
    }

    @Test
    void getAverageGradeNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/getAverageGrade",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/getAverageGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.ENTITY_NOT_FOUND)));
        }
    }

    @Test
    void getAverageGradeSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/getAverageGrade",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_FINAL);

            mockMvc.perform(post("/teacher_service/getAverageGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_FINAL)));
        }
    }
}