package app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import app.constants.Constants;
import java.util.stream.Stream;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthorisationServiceTest {

    transient AuthorisationService authorisationService;

    @BeforeEach
    void setup() {
        authorisationService = new AuthorisationService();
    }

    @ParameterizedTest(name = "teacher-teacherTask={0}, teacher-studentTask={1}, student-teacherTask={2}, student-studentTask={3}, tokenExpired={4}, incorrectSubject={5}, missingSubject={6}")
    @MethodSource("generator")
    void authorisationTest(boolean result, String sessionId, int requiredRole) {
        ResponseEntity<String> expected =
            new ResponseEntity<String>(
                new JSONObject().put(Constants.AUTHORISEDKEY, result).toString(),
                HttpStatus.OK);
        ResponseEntity<String> actual =
            authorisationService.verifyAuthentication(sessionId, requiredRole);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> generator() {
        return Stream.of(
            // teacher doing teacher task
            Arguments.of(true, Constants.SESSION_ID_TEACHER, 1),
            Arguments.of(true, Constants.SESSION_ID_TEACHER, 0),
            Arguments.of(false, Constants.SESSION_ID_STUDENT, 1),
            Arguments.of(true, Constants.SESSION_ID_STUDENT, 0),
            Arguments.of(false, Constants.SESSION_EXPIRED, 0),
            Arguments.of(false, Constants.SESSION_ID_WRONG_SUBJECT, 0),
            Arguments.of(false, Constants.SESSION_ID_MISSING_SUBJECT, 0)
        );
    }
}