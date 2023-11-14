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
@WebMvcTest(ExamQuestionsController.class)
class ExamQuestionsControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void getExamQuestionsNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/examQuestionsByStudentExamId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/getExamQuestions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content("{ \"id\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Error fetching exam questions")));
        }
    }

    @Test
    void getExamQuestionsSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8083/exam_service/examQuestionsByStudentExamId",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/getExamQuestions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content("{ \"id\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

}