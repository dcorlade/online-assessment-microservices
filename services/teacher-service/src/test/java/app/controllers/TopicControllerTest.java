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
@WebMvcTest(TopicController.class)
class TopicControllerTest {
    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void topicByIdNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/getTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/topicById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.TOPIC_NOT_FOUND)));
        }
    }

    @Test
    void topicByIdSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/getTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/topicById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void deleteTopicNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/deleteTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/deleteTopic")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.TOPIC_NOT_FOUND)));
        }
    }

    @Test
    void deleteTopicSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/deleteTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/deleteTopic")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString("Success")));
        }
    }

    @Test
    void createTopicNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/createTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/createTopic")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to create topic")));
        }
    }

    @Test
    void createTopicSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/createTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/createTopic")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void updateTopicNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/updateTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/updateTopic")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to update topic")));
        }
    }

    @Test
    void updateTopicSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/topic/updateTopic",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/updateTopic")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }


}