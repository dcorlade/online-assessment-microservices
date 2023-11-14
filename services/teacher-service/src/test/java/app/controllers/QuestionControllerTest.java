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
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void questionByIdNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/questionById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/questionById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.ENTITY_NOT_FOUND)));
        }
    }

    @Test
    void questionByIdSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/getQuestion",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/questionById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void deleteQuestionNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/deleteQuestion",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/deleteQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.ENTITY_NOT_FOUND)));
        }
    }

    @Test
    void deleteQuestionSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/deleteQuestion",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/deleteQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString("Success")));
        }
    }

    @Test
    void createQuestionNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/createQuestion",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/createQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to create")));
        }
    }

    @Test
    void createQuestionSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/createQuestion",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/createQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void updateQuestionNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/updateQuestion",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/updateQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to update")));
        }
    }

    @Test
    void updateQuestionSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/question/updateQuestion",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/updateQuestion")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }
}