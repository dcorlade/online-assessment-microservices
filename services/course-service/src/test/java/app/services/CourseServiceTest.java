package app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.communication.Authorisation;
import app.constants.Constants;
import app.models.Course;
import app.repositories.CourseRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CourseServiceTest {

    @InjectMocks
    private static transient CourseService courseService;

    @Mock
    transient CourseRepository courseRepository;
    transient Course course =
        new Course(0, "courseCode", "name", "2020", new ArrayList<>(), new ArrayList<>());
    transient List<Course> queryResult = new ArrayList<Course>();

    @BeforeEach
    void setup() {
        queryResult.add(course);
    }

    @Test
    void addCourseUnauthorised() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 0))
                .thenReturn(false);

            assertNotEquals(HttpStatus.OK,
                courseService.addCourse(course, Constants.SESSION_ID_STUDENT).getStatusCode());
            verify(courseRepository, times(0)).save(course);
        }
    }


    @Test
    void addCourseSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 1))
                .thenReturn(true);
            courseService.addCourse(course, Constants.SESSION_ID_TEACHER);
            verify(courseRepository, times(1)).save(course);
        }
    }

    @Test
    void updateCourseUnauthorised() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 0))
                .thenReturn(false);

            assertNotEquals(HttpStatus.OK,
                courseService.updateCourse(course, Constants.SESSION_ID_STUDENT).getStatusCode());
            verify(courseRepository, times(0)).save(course);
        }
    }

    @Test
    void updateCourseNotPresent() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 1))
                .thenReturn(true);

            when(courseRepository.findByCourseCode(course.getCourseCode()))
                .thenReturn(new ArrayList<Course>());

            assertNotEquals(HttpStatus.OK,
                courseService.updateCourse(course, Constants.SESSION_ID_TEACHER).getStatusCode());
            verify(courseRepository, times(0)).save(course);
        }
    }

    @Test
    void updateCourseSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 1))
                .thenReturn(true);

            when(courseRepository.findByCourseCode(course.getCourseCode())).thenReturn(queryResult);
            courseService.updateCourse(course, Constants.SESSION_ID_TEACHER);
            verify(courseRepository, times(1)).save(course);
        }
    }

    @Test
    void getCourseUnauthorised() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 0))
                .thenReturn(false);

            assertNotEquals(HttpStatus.OK,
                courseService.getCourse(course.getId(), Constants.SESSION_ID_STUDENT)
                    .getStatusCode());
            verify(courseRepository, times(0)).findById(course.getId());
        }
    }

    @Test
    void getCourseNotPresent() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 1))
                .thenReturn(true);

            when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());
            assertNotEquals(HttpStatus.OK,
                courseService.getCourse(course.getId(), Constants.SESSION_ID_TEACHER)
                    .getStatusCode());
            verify(courseRepository, times(1)).findById(course.getId());
        }
    }

    @Test
    void getCourseSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 1))
                .thenReturn(true);

            when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

            ResponseEntity<String> result =
                courseService.getCourse(course.getId(), Constants.SESSION_ID_TEACHER);
            assertEquals(HttpStatus.OK, result.getStatusCode());
            verify(courseRepository, times(1)).findById(course.getId());
        }
    }

    @Test
    void deleteCourseUnauthorised() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 0))
                .thenReturn(false);

            assertNotEquals(HttpStatus.OK,
                courseService.deleteCourse(course, Constants.SESSION_ID_STUDENT).getStatusCode());
            verify(courseRepository, times(0)).save(course);
        }
    }

    @Test
    void deleteCourseNotPresent() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 1))
                .thenReturn(true);

            when(courseRepository.findByCourseCode(course.getCourseCode()))
                .thenReturn(new ArrayList<Course>());
            assertNotEquals(HttpStatus.OK,
                courseService.deleteCourse(course, Constants.SESSION_ID_TEACHER).getStatusCode());
            verify(courseRepository, times(0)).delete(course);
        }
    }

    @Test
    void deleteCourseSuccess() {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_TEACHER, 1))
                .thenReturn(true);

            when(courseRepository.findByCourseCode(course.getCourseCode())).thenReturn(queryResult);
            assertEquals(HttpStatus.OK,
                courseService.deleteCourse(course, Constants.SESSION_ID_TEACHER).getStatusCode());
            verify(courseRepository, times(1)).delete(course);
        }
    }
}