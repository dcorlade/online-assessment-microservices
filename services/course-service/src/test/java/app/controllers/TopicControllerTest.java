package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Topic;
import app.serializerfactory.Serializer;
import app.services.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TopicController.class)
class TopicControllerTest {
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    transient Topic topic =
            new Topic(1, "name", null, 1, null);
    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient TopicService topicService;

    @BeforeEach
    void setup() {
        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(serializer.serialize(topic), HttpStatus.OK);
        when(topicService.getTopic(1, Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
        when(topicService
                .addTopic(topic, Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
        when(topicService.updateTopic(topic,
                Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
        when(topicService.getTopicsByCourse(1, Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
    }

    @Test
    void getTopic() {
        try {
            mockMvc.perform(post("/courseService/topic/getTopic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content("{\"id\":" + topic.getId() + "}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(topic)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void getTopicsByCourseId() {
        try {
            mockMvc.perform(post("/courseService/topic/getTopicsByCourseId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content("{\"id\":" + topic.getCourseId() + "}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(topic)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void updateTopic() {
        try {
            mockMvc.perform(post("/courseService/topic/updateTopic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(topic))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(topic)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void addTopic() {
        try {
            mockMvc.perform(post("/courseService/topic/createTopic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(topic))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(topic)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void deleteAnswer() {
        try {
            mockMvc.perform(post("/courseService/topic/deleteTopic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(topic))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }
}