package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.serializerfactory.Serializer;
import app.services.AnswerService;
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
@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    transient Answer answer =
            new Answer(1, null, 1, 1, "description", true);

    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient AnswerService answerService;

    @BeforeEach
    void setup() {
        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(serializer.serialize(answer), HttpStatus.OK);
        when(answerService.getAnswer(1, Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
        when(answerService.getAnswersByQuestion(1,
                Constants.SESSION_ID_TEACHER)).thenReturn(responseEntity);
        when(answerService
                .updateAnswer(answer, Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
        when(answerService
                .addAnswer(answer, Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
    }


    @Test
    void getAnswer() {
        try {
            mockMvc.perform(post("/courseService/answer/getAnswer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content("{\"id\":" + answer.getId() + "}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(answer)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void getAnswersByQuestion() {
        try {
            mockMvc
                    .perform(post("/courseService/answer/getAnswerByQuestionId")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                            .content("{\"id\":" + answer.getQuestionId() + "}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(answer)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void createAnswer() {
        try {
            mockMvc.perform(post("/courseService/answer/createAnswer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(answer))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(answer)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void updateAnswer() {
        try {
            mockMvc.perform(post("/courseService/answer/updateAnswer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(answer))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(answer)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void deleteAnswer() {
        try {
            mockMvc.perform(post("/courseService/answer/deleteAnswer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(answer))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }
}