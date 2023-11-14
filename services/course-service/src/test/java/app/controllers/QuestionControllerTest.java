package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Question;
import app.serializerfactory.Serializer;
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
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    transient Question question =
            new Question(1, null, 1, "Title", "description", null);
    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient app.services.QuestionService questionService;

    @BeforeEach
    void setup() {
        ResponseEntity<String> responseEntity =
                new ResponseEntity<>(serializer.serialize(question), HttpStatus.OK);
        when(questionService.getQuestionsByTopic(1,
                Constants.SESSION_ID_TEACHER)).thenReturn(responseEntity);
        when(questionService
                .addQuestion(
                        question,
                        Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
        when(questionService
                .updateQuestion(
                        question,
                        Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);
        when(questionService
                .deleteQuestion(
                        1,
                        Constants.SESSION_ID_TEACHER))
                .thenReturn(responseEntity);


    }

    @Test
    void getQuestion() {
        try {
            mockMvc.perform(post("/courseService/question/getQuestion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content("{\"id\":" + question.getId() + "}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void getQuestionsByTopicId() {
        try {
            mockMvc
                    .perform(post("/courseService/question/getQuestionByTopicId")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                            .content("{\"id\":" + question.getTopicId() + "}")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(question)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void createQuestion() {
        try {
            mockMvc.perform(post("/courseService/question/createQuestion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(question))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(question)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void updateQuestion() {
        try {
            mockMvc.perform(post("/courseService/question/updateQuestion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(question))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(question)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void deleteQuestion() {
        try {
            mockMvc.perform(post("/courseService/question/deleteQuestion")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content(serializer.serialize(question))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(serializer.serialize(question)));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }
}