package app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Enrollment;
import app.repositories.EnrollmentRepository;
import app.serializerfactory.Serializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EnrollmentService.class)
public class EnrollmentServiceTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient EnrollmentRepository enrollmentRepository;
    private transient EnrollmentService enrollmentService;
    private transient Integer courseId1;
    private transient String userId1;
    private transient Enrollment enrollment1;
    private transient Integer courseId2;
    private transient String userId2;
    private transient Enrollment enrollmentNotYetAdded;
    private transient List<Enrollment> allEnrollments;

    @BeforeEach
    void setup() {
        enrollmentService = new EnrollmentService(enrollmentRepository);

        courseId1 = 1;
        userId1 = "netIdStudent";
        enrollment1 = new Enrollment();
        enrollment1.setId(1);
        enrollment1.setCourseId(courseId1);
        enrollment1.setUserId(userId1);

        courseId2 = 2;
        userId2 = "user2";
        enrollmentNotYetAdded = new Enrollment();
        enrollmentNotYetAdded.setId(2);
        enrollmentNotYetAdded.setCourseId(courseId2);
        enrollmentNotYetAdded.setUserId("netIdStudent");

        allEnrollments = new ArrayList<>();
        allEnrollments.add(enrollment1);

        doReturn(Optional.of(enrollment1)).when(enrollmentRepository).findById(1);
        doReturn(allEnrollments).when(enrollmentRepository).findAll();
        doReturn(allEnrollments).when(enrollmentRepository)
            .findEnrollmentByCourseIdAndUserId(1, "netIdStudent");
    }

    @Test
    void getEnrollmentTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.getEnrollment(enrollment1.getId(), Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(serializer.serialize(enrollment1), res.getBody());
        }
    }

    @Test
    void getEnrollmentTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.getEnrollment(Constants.NON_EXISTING_ENROLLMENT_ID,
                    Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getEnrollmentTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res = enrollmentService.getEnrollment(enrollment1.getId(), "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void getEnrollmentByCourseTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.getEnrollmentByCourse(courseId1, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(
                new JSONObject()
                    .put("enrollments", new JSONArray().put(serializer.serialize(enrollment1)))
                    .toString(), res.getBody());
        }
    }

    @Test
    void getEnrollmentByCourseTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.getEnrollmentByCourse(Constants.NON_EXISTING_COURSE_ID,
                    Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getEnrollmentByCourseAndUserTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            doReturn(allEnrollments).when(enrollmentRepository)
                .findEnrollmentByCourseIdAndUserId(courseId1, userId1);

            ResponseEntity<String> res =
                enrollmentService
                    .getEnrollmentByCourseAndUser(courseId1, userId1, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(serializer.serialize(enrollment1), res.getBody());
        }
    }

    @Test
    void getEnrollmentByCourseAndUserTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.getEnrollmentByCourse(Constants.NON_EXISTING_COURSE_ID,
                    Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getEnrollmentByCourseTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res =
                enrollmentService.getEnrollmentByCourse(Constants.NON_EXISTING_COURSE_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void getEnrollmentByUserTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.getEnrollmentByUser(userId1, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertEquals(
                new JSONObject()
                    .put("enrollments", new JSONArray().put(serializer.serialize(enrollment1)))
                    .toString(), res.getBody());
        }
    }

    @Test
    void getEnrollmentByUserTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.getEnrollmentByUser(Constants.NON_EXISTING_USER_ID,
                    Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void getEnrollmentByUserTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);

            ResponseEntity<String> res =
                enrollmentService.getEnrollmentByUser(Constants.NON_EXISTING_USER_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void addEnrollmentTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService
                    .addEnrollment(enrollmentNotYetAdded, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void addEnrollmentTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.addEnrollment(enrollment1, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
        }
    }

    @Test
    void addEnrollmentTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res = enrollmentService.addEnrollment(enrollment1, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void updateEnrollmentTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService.updateEnrollment(enrollment1, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void updateEnrollmentTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService
                    .updateEnrollment(enrollmentNotYetAdded, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void updateEnrollmentTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 1))
                .thenReturn(false);

            ResponseEntity<String> res =
                enrollmentService.updateEnrollment(enrollment1, Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void deleteEnrollmentTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService
                    .deleteEnrollment(enrollment1.getId(), Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void deleteEnrollmentTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService
                    .deleteEnrollment(enrollmentNotYetAdded.getId(), Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        }
    }

    @Test
    void deleteEnrollmentTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);

            ResponseEntity<String> res =
                enrollmentService.deleteEnrollment(Constants.NON_EXISTING_ENROLLMENT_ID, "");

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void deleteEnrollmentByUserAndCourseTestSuccess() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res =
                enrollmentService
                    .deleteEnrollmentByUserAndCourse(userId1, courseId1,
                        Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.OK, res.getStatusCode());
        }
    }

    @Test
    void deleteEnrollmentByUserAndCourseTestFailure() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);

            ResponseEntity<String> res = enrollmentService
                .deleteEnrollmentByUserAndCourse(Constants.NON_EXISTING_USER_ID,
                    Constants.NON_EXISTING_COURSE_ID,
                    Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }

    @Test
    void deleteEnrollmentByUserAndCourseTestNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 1))
                .thenReturn(false);

            ResponseEntity<String> res = enrollmentService
                .deleteEnrollmentByUserAndCourse(Constants.NON_EXISTING_USER_ID,
                    Constants.NON_EXISTING_COURSE_ID,
                    Constants.SESSION_ID_STUDENT);

            assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
        }
    }
}
