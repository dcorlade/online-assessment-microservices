package app.controllers;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Course;
import app.serializerfactory.Serializer;
import app.services.CourseService;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    transient Course course =
        new Course(0, "courseCode", "name", "2020", new ArrayList<>(), new ArrayList<>());
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient CourseService courseService;

    @Test
    void createCourse() {
        try {
            mockMvc.perform(post("/courseService/course/createCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content(serializer.serialize(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void getCourse() {
        try {
            mockMvc.perform(post("/courseService/course/courseById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content("{\"id\":" + course.getId() + "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void updateCourse() {
        try {
            mockMvc.perform(post("/courseService/course/updateCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content(serializer.serialize(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void deleteCourse() {
        try {
            mockMvc.perform(post("/courseService/course/deleteCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content(serializer.serialize(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }
}