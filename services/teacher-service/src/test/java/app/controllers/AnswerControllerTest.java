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
@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void answerByIdNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/getAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/answerById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.ENTITY_NOT_FOUND)));
        }
    }

    @Test
    void answerByIdSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/getAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/answerById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void getAnswerByQuestionIdNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/getAnswerByQuestionId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/getAnswerByQuestionId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Question ID not found")));
        }
    }

    @Test
    void getAnswerByQuestionIdSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/getAnswerByQuestionId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/getAnswerByQuestionId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void updateAnswerNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/updateAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/updateAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to update")));
        }
    }

    @Test
    void updateAnswerSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/updateAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/updateAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void createAnswerNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/createAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/createAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to create")));
        }
    }

    @Test
    void createAnswerSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/createAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/createAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void deleteAnswerNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/deleteAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/deleteAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.ENTITY_NOT_FOUND)));
        }
    }

    @Test
    void deleteAnswerSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/answer/deleteAnswer",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/deleteAnswer")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString("Success")));
        }
    }
}