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
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @Test
    void courseByIdNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/courseById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/courseById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.COURSE_NOT_FOUND)));
        }
    }

    @Test
    void courseByIdSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/courseById",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/courseById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void deleteCourseNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/deleteCourse",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/deleteCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString(Constants.COURSE_NOT_FOUND)));
        }
    }

    @Test
    void deleteCourseSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/deleteCourse",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/deleteCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString("Success")));
        }
    }

    @Test
    void createCourseNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/createCourse",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/createCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to create course")));
        }
    }

    @Test
    void createCourseSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/createCourse",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/createCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }

    @Test
    void updateCourseNull() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/updateCourse",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(null);

            mockMvc.perform(post("/teacher_service/updateCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(404)))
                .andExpect(content().string(containsString("Failed to update course")));
        }
    }

    @Test
    void updateCourseSuccess() throws Exception {
        try (MockedStatic<TeacherServiceCommunication> mockedStatic
                 = Mockito.mockStatic(TeacherServiceCommunication.class)) {
            mockedStatic.when(() -> TeacherServiceCommunication.postRequest(Constants.ID_1,
                "8082/courseService/course/updateCourse",
                Constants.TEST_SESSION_TOKEN))
                .thenReturn(Constants.NAME_SEM);

            mockMvc.perform(post("/teacher_service/updateCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
                .content(Constants.ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect((status().is(200)))
                .andExpect(content().string(containsString(Constants.NAME_SEM)));
        }
    }
}