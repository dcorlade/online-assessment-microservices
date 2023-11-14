package app.controllers;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Course;
import app.models.Enrollment;
import app.models.User;
import app.serializerfactory.Serializer;
import app.services.EnrollmentService;
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
@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    transient Enrollment enrollment =
        new Enrollment(1, "userId", 1, null);
    transient Course course = new Course(1, "courseCode", "name", "year", null, null);
    transient User user = new User("netId", "password", 1, 1);
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient EnrollmentService enrollmentService;

    @Test
    void getEnrollment() {
        try {
            mockMvc.perform(post("/courseService/enrollment/getEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content("{\"id\":" + enrollment.getId() + "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void getEnrollmentByCourse() {
        try {
            mockMvc.perform(
                post("/courseService/enrollment/getEnrollmentByCourse")
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
    void getEnrollmentByUser() {
        try {
            mockMvc.perform(
                post("/courseService/enrollment/getEnrollmentByUser/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                    .content("{\"net_id\":" + user.getNetId() + "}")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void addEnrollment() {
        try {
            mockMvc.perform(post("/courseService/enrollment/addEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content(serializer.serialize(enrollment))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void updateEnrollment() {
        try {
            mockMvc.perform(post("/courseService/enrollment/updateEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content(serializer.serialize(enrollment))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void deleteEnrollment() {
        try {
            mockMvc.perform(post("/courseService/enrollment/deleteEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content("{\"id\":" + enrollment.getId() + "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }

    @Test
    void deleteEnrollmentByUserAndCourse() {
        try {
            mockMvc.perform(post("/courseService/enrollment/deleteTopicByUserAndCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_TEACHER)
                .content("{\"net_id\":" + user.getNetId() + ",id:" + course.getId() + "}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail(Constants.EXCEPTION_MESSAGE);
        }
    }
}