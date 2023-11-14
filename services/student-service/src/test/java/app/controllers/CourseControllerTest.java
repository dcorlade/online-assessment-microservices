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
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void courseByIdNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/courseById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/courseById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Course not found")));
        }
    }

    @Test
    void courseByIdSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/courseById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/courseById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void enrollNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/enrollment/addEnrollment",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Course not found")));
        }
    }

    @Test
    void enrollSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/enrollment/addEnrollment",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void unEnrollNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/enrollment/deleteEnrollment",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/unEnroll")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Enrollment not found")));
        }
    }

    @Test
    void unEnrollSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/enrollment/deleteEnrollment",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn("");

            mockMvc.perform(post("/student_service/unEnroll")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString("Success")));
        }
    }

    @Test
    void getEnrollmentNull() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/enrollment/getEnrollmentByUser",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/student_service/getEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Could not get enrollments for user")));
        }
    }

    @Test
    void getEnrollmentSuccess() throws Exception {
        try (MockedStatic<StudentServiceCommunication> mockedStatic
                 = Mockito.mockStatic(StudentServiceCommunication.class)) {
            mockedStatic.when(() -> StudentServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/enrollment/getEnrollmentByUser",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/student_service/getEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

}