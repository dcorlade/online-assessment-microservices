package app.constants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import javax.crypto.SecretKey;

public class Constants {

    // GENERAL CONSTANTS
    public static final String SESSIONHEADERKEY = "session";

    /**
     * This key is used throughout the application to both encode and decode the session information
     * into the Java Web Token.
     */
    public static final SecretKey SECRET_KEY =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(
            "SXVHMsZyT/qJIRBWtypb3Kobjiyh12/aoQ77si6cuyTB+"
                + "+vrIfEZsXEXDDfPPUD2Vz0GzUYZlcgqnYRJdwntzg=="));

    public static String ROLE_KEY = "role";
    public static String NET_ID_KEY = "netId";
    public static String NET_ID_STUDENT = "netIdStudent";
    /**
     * This is an example session-jwt of a student. It can be used for testing methods that require
     * a Java Web Token with session information.
     */
    public static final String SESSION_ID_STUDENT = Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject(SESSIONHEADERKEY)
        .setExpiration(Date.valueOf(LocalDate.ofYearDay(3000, 1)))
        .claim(NET_ID_KEY, NET_ID_STUDENT)
        .claim(ROLE_KEY, 0)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
        .compact();
    private static String netIdTeacher = "netIdTeacher";
    /**
     * This is an example session-jwt of a teacher. It can be used for testing methods that require
     * a Java Web Token with session information.
     */
    public static final String SESSION_ID_TEACHER = Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject(SESSIONHEADERKEY)
        .setExpiration(Date.valueOf(LocalDate.ofYearDay(3000, 1)))
        .claim(NET_ID_KEY, netIdTeacher)
        .claim(ROLE_KEY, 1)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
        .compact();

    /**
     * This is an example session-jwt that is expired. It can be used for testing if
     * this token does indeed not get permission to perform an action.
     */
    public static final String SESSION_EXPIRED = Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject(SESSIONHEADERKEY)
        .setExpiration(Date.valueOf(LocalDate.now().minusDays(1L)))
        .claim(NET_ID_KEY, netIdTeacher)
        .claim(ROLE_KEY, 1)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
        .compact();

    /**
     * This is an example session-jwt that has the wrong subject set in the header. It can be used
     * for testing if this token does indeed not get permission to perform an action.
     */
    public static final String SESSION_ID_WRONG_SUBJECT = Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject("random")
        .setExpiration(Date.valueOf(LocalDate.ofYearDay(3000, 1)))
        .claim(NET_ID_KEY, netIdTeacher)
        .claim(ROLE_KEY, 1)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
        .compact();

    /**
     * This is an example session-jwt that is missing the subject in the header. It can be used
     * for testing if this token does indeed not get permission to perform an action.
     */
    public static final String SESSION_ID_MISSING_SUBJECT = Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setExpiration(Date.valueOf(LocalDate.ofYearDay(3000, 1)))
        .claim(NET_ID_KEY, netIdTeacher)
        .claim(ROLE_KEY, 1)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
        .compact();

    /**
     * This is an example session-jwt that is missing a value for netID.
     */
    public static final String SESSION_ID_EMPTY = Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject(SESSIONHEADERKEY)
        .setExpiration(Date.valueOf(LocalDate.ofYearDay(3000, 1)))
        .claim("netId", "")
        .claim("role", 0)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
        .compact();

    // AUTHENTICATION CONSTANTS
    public static final String HEADER_STRING = "Authorisation";
    public static final String SIGN_UP_URL = "/authentication/sign-up";
    public static final int ROLE_NUMBER = 1;
    public static final String PASSWORD_STRING = "password";
    public static final String ROLE_STRING = "role";
    public static final String NULL_STRING = "null";
    public static final String TEST = "test";


    // AUTHORISATION CONSTANTS
    public static String AUTHORISEDKEY = "authorised";

    // COURSE-SERVICE CONSTANTS
    public static final String NOT_AUTHORIZED_STRING = "The user is not authorised for this action";
    public static final String NOT_AUTHORIZED_STRING_EXAM = "User is not authorized";
    public static final String NO_ENROLLMENT = "There exists no enrollment with that id";
    public static final String TEST_SESSION_TOKEN = "yrceuiay34n8qrty34oqt7yve8na";
    public static final String HOST = "localhost";
    public static final int PORT8081 = 8081;
    public static final int PORT8083 = 8083;
    public static final String RESPONSE_HEADER_NAME = "Content-Type";
    public static final String RESPONSE_HEADER_VALUE = "application/json; charset=utf-8";
    public static final String PATH = "/authorisation/getAuthorisation/";
    public static final String RESPONSE_BODY = "{ response: 'bar' }";
    public static final String EXCEPTION_MESSAGE = "The test threw an exception";
    public static final Integer NON_EXISTING_ANSWER_ID = 10;
    public static final Integer NON_EXISTING_QUESTION_ID = 10;
    public static final Integer NON_EXISTING_ENROLLMENT_ID = 10;
    public static final Integer NON_EXISTING_COURSE_ID = 10;
    public static final Integer NON_EXISTING_TOPIC_ID = 10;
    public static final String NON_EXISTING_USER_ID = "user10";
    public static final String COURSE_ID_1 = "{\"courseId\":1}";
    public static final String TOO_MANY_TOPICS =
        "There are more than 10 topics related to this course";
    public static final String TOPIC_WITHOUT_Q = "There are topics without questions";
    public static final String NOT_ENOUGH_Q = "There are not enough questions";
    public static final String QUESTION_IDS = "{\"questionIds\":\"[0,1,2,3,4,5,6,7,8,9]\"}";
    public static final String QUESTION_STRING =
        "{\"questions\":\"[\\\"{\\\\\\\"answers\\\\\\\":[\\\\\\\""
            + "{\\\\\\\\\\\\\\\"id\\\\\\\\\\\\\\\":1}\\\\\\\"],\\\\\\\"id\\\\\\\":0}"
            + "\\\",\\\"{\\\\\\\"answers\\\\\\\":[\\\\\\\"{\\\\\\\\\\\\\\\"id\\\\\\\\"
            + "\\\\\\\":1}\\\\\\\"],\\\\\\\"id\\\\\\\":1}\\\",\\\"{\\\\\\\"answers\\\\\\"
            + "\":[\\\\\\\"{\\\\\\\\\\\\\\\"id\\\\\\\\\\\\\\\":1}\\\\\\\"],\\\\\\\"i"
            + "d\\\\\\\":2}\\\",\\\"{\\\\\\\"answers\\\\\\\":[\\\\\\\""
            + "{\\\\\\\\\\\\\\\"id\\\\\\\\\\\\\\\":1}\\\\\\\"],\\\\\\\"id\\\\\\\":3}"
            + "\\\",\\\"{\\\\\\\"answers\\\\\\\":[\\\\\\\"{\\\\\\\\\\\\\\\"id\\\\\\\\"
            + "\\\\\\\":1}\\\\\\\"],\\\\\\\"id\\\\\\\":4}\\\",\\\"{\\\\\\\"answers\\\\"
            + "\\\":[\\\\\\\"{\\\\\\\\\\\\\\\"id\\\\\\\\\\\\\\\":1}\\\\\\\"],\\\\\\\"i"
            + "d\\\\\\\":5}\\\",\\\"{\\\\\\\"answers\\\\\\\":[\\\\\\\"{\\\\\\\\\\\\"
            + "\\\"id\\\\\\\\\\\\\\\":1}\\\\\\\"],\\\\\\\"id\\\\\\\":6}\\\",\\\"{\\"
            + "\\\\\"answers\\\\\\\":[\\\\\\\"{\\\\\\\\\\\\\\\"id\\\\\\\\\\\\\\\""
            + ":1}\\\\\\\"],\\\\\\\"id\\\\\\\":7}\\\",\\\"{\\\\\\\"answers\\\\\\"
            + "\":[\\\\\\\"{\\\\\\\\\\\\\\\"id\\\\\\\\\\\\\\\":1}\\\\\\\"],\\\\\\\""
            + "id\\\\\\\":8}\\\",\\\"{\\\\\\\"answers\\\\\\\":[\\\\\\\"{\\\\\\\\\\"
            + "\\\\\"id\\\\\\\\\\\\\\\":1}\\\\\\\"],\\\\\\\"id\\\\\\\":9}\\\"]\"}";

    public static final String POST_REQUEST = "/course_service/examQuestionsByCourseId";

    //Exam-Service constants
    public static final String NOT_FOUND = "Entity not found";
    public static final String EXAM_ID = "examId";
    public static final int THREE = 3;
    public static final String USER_ID_STRING = "userId";
    public static final String GET_AMOUNT_OF_STUDENTS_STRING = "/exam_service/getAmountOfStudents";
    public static final String EXAM_QUESTION_ID = "{\"examQuestionId\": 0}";
    public static final String DELETION = "Deletion was successful";
    public static final String COURSE_ID_5 = "{\"courseId\":5}";
    public static final String EXAM_QUESTION_BY_COURSE =
        "8082/course_service/examQuestionsByCourseId";
    public static final String GET_QUESTIONS_BY_ID = "8082/course_service/getQuestionsById";
    public static final String CREATE_STUDENT_EXAM_STRING = "/exam_service/createStudentExam";
    public static final String SUBMIT_EXAM = "/exam_service/submitStudentExam";
    public static final String GET_EXTRA_TIME = "8080/authentication_service/getExtraTime";
    public static final String EXAM_QUESTION =
        "{\"examId\":2,\"userId\":\"1\",\"id\":1,\"selected\":false}";
    public static final String ID_1 = "{ \"id\": 1}";
    public static final String ID_1_EXAM = "{\"id\":1}";
    public static final String USER_ID_1 = "{\"userId\":\"1\"}";
    public static final String EXTRA_TIME_10 = "{\"extraTime\":10}";
    public static final String USER_1_COURSE_ID_5 = "{\"userId\":1,\"courseId\":5}";
    public static final String USER_2_COURSE_ID_5 = "{\"userId\":2,\"courseId\":5}";
    public static final String GET_LEAST_ANSWERED = "/exam_service/getLeastAnsweredQuestions";
    public static final String JWT =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicGF3YW4iLCJyb2xlIjpudWxsLCJlbmFibGUiOm51"
            + "bGwsImV4cCI6MTUzNDgyMzUyNSwiaWF0IjoxNTM0Nzk0NzI1fQ.65PPkn"
            + "MebR53ykLm-EBIunjFJvlV-vL-pfTOtbBLtnQ";
    public static final String STUDENT_EXAM_BY_USER_ID = "/exam_service/studentExamByUserId";
    public static final String ENROLLMENT_STRING = "{\"id\": 1}";
    public static final String GET_ENROLLMENT =
        "8082/course_service/getEnrollmentByCourseAndUser/";

    // STUDENT AND TEACHER SERVICE CONSTANTS
    public static final String STUDENT_ANSWER_NOT_FOUND = "Student answer not found";
    public static final String STUDENT_EXAM_NOT_FOUND = "Student exam not found";
    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String COURSE_NOT_FOUND = "Course not found";
    public static final String TOPIC_NOT_FOUND = "Topic not found";
    public static final String NAME_SEM = "{ \"name\": SEM}";
    public static final String NAME_FINAL = "{ \"name\": Final}";
    public static final String UPDATE_EXAM = "8083/exam_service/updateExam";


    //SERIALIZER
    public static final String ORDER_STRING = "order";
    public static final String DESCRIPTION_STRING = "description";
    public static final String CORRECT_STRING = "correct";
    public static final String NAME_STRING = "name";
    public static final String YEAR_STRING = "year";
    public static final String START_STRING = "start";
    public static final String END_STRING = "end";
    public static final String QUESTION_STRING_FACTORY = "question";
    public static final String TITLE_STRING = "title";
    public static final String ANSWER_STRING = "answer";
    public static final String SELECTED_STRING = "selected";
    public static final String GRADE_STRING = "grade";
    public static final String COURSE_ID = "courseId";
    public static final String EXTRA_TIME = "extraTime";
}

