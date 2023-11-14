package app.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.communication.Authorisation;
import app.communication.ExamServiceCommunication;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.models.Course;
import app.models.Exam;
import app.models.ExamQuestion;
import app.models.Question;
import app.models.StudentAnswer;
import app.models.StudentExam;
import app.repositories.ExamRepository;
import app.repositories.StudentExamRepository;
import app.serializerfactory.Serializer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentExamController.class)
class StudentExamControllerTest {

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient StudentExamRepository studentExamRepository;
    @MockBean
    private transient ExamRepository examRepository;
    private transient StudentExamController controller;
    private transient StudentExam studentExam1;
    private transient StudentExam studentExam3;
    private transient StudentExam studentExam4;
    private transient Exam exam1;
    private transient JSONObject studentExamsJson;
    private transient JSONObject questionListJson;
    private transient List<ExamQuestion> examQuestions;
    private transient List<Question> questionList;
    private transient StudentExamSupport support;

    @BeforeEach
    void setup() {
        support = new StudentExamSupport(studentExamRepository);
        StudentAnswer studentAnswer1 = new StudentAnswer();
        StudentAnswer studentAnswer2 = new StudentAnswer();
        studentAnswer1.setSelected(true);
        studentAnswer2.setSelected(false);

        List<StudentAnswer> studentAnswerList1 = new ArrayList<>();
        List<StudentAnswer> studentAnswerList2 = new ArrayList<>();
        studentAnswerList1.add(studentAnswer1);
        studentAnswerList2.add(studentAnswer2);


        ExamQuestion examQuestion1 = new ExamQuestion();
        examQuestion1.setId(1);
        examQuestion1.setQuestion(1);
        examQuestion1.setStudentAnswers(studentAnswerList1);
        ExamQuestion examQuestion2 = new ExamQuestion();
        examQuestion2.setId(2);
        examQuestion2.setQuestion(2);
        examQuestion2.setStudentAnswers(studentAnswerList2);

        Answer answer1 = new Answer();
        Answer answer2 = new Answer();
        answer1.setCorrect(true);
        answer2.setCorrect(true);

        List<Answer> answerList1 = new ArrayList<>();
        List<Answer> answerList2 = new ArrayList<>();
        answerList1.add(answer1);
        answerList2.add(answer2);

        Question question1 = new Question();
        Question question2 = new Question();
        question1.setId(1);
        question1.setAnswers(answerList1);
        question2.setId(2);
        question2.setAnswers(answerList2);

        questionList = new ArrayList<>();
        questionList.add(question1);
        questionList.add(question2);

        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.put(serializer.serialize(question1));
        jsonArray1.put(serializer.serialize(question2));
        questionListJson = new JSONObject();
        questionListJson.put("questions", jsonArray1);

        examQuestions = new ArrayList<>();
        examQuestions.add(examQuestion1);
        examQuestions.add(examQuestion2);

        studentExam1 = new StudentExam();
        studentExam1.setId(1);
        studentExam1.setUser("1");
        studentExam1.setExamId(1);
        studentExam1.setStartingTime(new Timestamp(System.currentTimeMillis()));
        studentExam1.setExamQuestions(examQuestions);

        StudentExam studentExam2 = new StudentExam();
        studentExam2.setId(2);
        studentExam2.setCorrectQuestions(9);
        studentExam2.setGrade(7.0f);
        studentExam2.setExtraTime(20);
        studentExam2.setUser("1");
        studentExam2.setExamId(1);
        studentExam2.setStartingTime(new Timestamp(System.currentTimeMillis()));

        studentExam3 = new StudentExam();
        studentExam3.setId(1);
        studentExam3.setCorrectQuestions(0);
        studentExam3.setExtraTime(10);
        studentExam3.setUser("1");
        studentExam3.setExamId(1);
        studentExam3.setStartingTime(new Timestamp(System.currentTimeMillis() - 1000000L));
        studentExam3.setExamQuestions(examQuestions);

        studentExam4 = new StudentExam();
        studentExam4.setId(1);
        studentExam4.setCorrectQuestions(1);
        studentExam4.setGrade(5.5F);
        studentExam4.setExtraTime(10);
        studentExam4.setUser("1");
        studentExam4.setExamId(1);
        studentExam4.setStartingTime(new Timestamp(System.currentTimeMillis() - 1000000L));
        studentExam4.setExamQuestions(examQuestions);

        exam1 = new Exam();
        exam1.setId(1);
        exam1.setCourseId(5);

        Course course1 = new Course();
        course1.setCourseCode("hello");
        course1.setId(1);

        List<StudentExam> studentExams = new ArrayList<>();
        studentExams.add(studentExam1);
        studentExams.add(studentExam2);

        List<StudentExam> studentExams1 = new ArrayList<>();
        studentExams1.add(studentExam1);
        studentExams1.add(studentExam1);
        studentExams1.add(studentExam1);
        studentExams1.add(studentExam1);

        JSONArray jsonArray2 = new JSONArray();
        jsonArray2.put(serializer.serialize(studentExam1));
        jsonArray2.put(serializer.serialize(studentExam2));
        studentExamsJson = new JSONObject();
        studentExamsJson.put("studentExamList", jsonArray2);

        doReturn(studentExam1).when(studentExamRepository).findById(1);
        doReturn(studentExams).when(studentExamRepository).findByExamId(10);
        doNothing().when(studentExamRepository).deleteById(1);
        doReturn(studentExams).when(studentExamRepository).findByExamAndUser(1, "1");
        doReturn(studentExams1).when(studentExamRepository).findByExamAndUser(2, "2");
        doReturn(exam1).when(examRepository).findById(1);
        doReturn(exam1).when(examRepository).findById(2);
        doReturn(studentExams).when(studentExamRepository).findByUser("netIdStudent");
    }


    @Test
    void studentExamByNetId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic
                .when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);
            mockMvc.perform(post(Constants.STUDENT_EXAM_BY_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_STUDENT)
                .content("{ \"userId\": netIdStudent}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(studentExamsJson.toString()));
        }
    }

    @Test
    void studentExamByNetIdNotSameNetId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic
                .when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);
            mockMvc.perform(post(Constants.STUDENT_EXAM_BY_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_STUDENT)
                .content("{ \"userId\": \"2\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void studentExamByNetIdNoNetId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic
                .when(() -> Authorisation.getAuthorisation(Constants.SESSION_ID_STUDENT, 0))
                .thenReturn(true);
            mockMvc.perform(post(Constants.STUDENT_EXAM_BY_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_STUDENT)
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(Constants.NOT_FOUND));
        }
    }

    @Test
    void studentExamByNetIdNotAuthorized() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation(Constants.JWT, 0))
                .thenReturn(false);
            mockMvc.perform(post(Constants.STUDENT_EXAM_BY_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, Constants.JWT)
                .content("{ \"userId\": \"2\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void studentExamById() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/studentExamById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(serializer.serialize(studentExam1))));
        }
    }

    @Test
    void notAuthorizedExceptionStudentExamById() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/studentExamById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"studentExamId\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void studentExamByIdNoEntity() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/studentExamById")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"id\": -1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(Constants.NOT_FOUND));
        }
    }

    @Test
    void getStudentExamsByExamId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/getStudentExamsByExamId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"examId\": 10}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(studentExamsJson.toString())));
        }
    }

    @Test
    void notAuthorizedExceptionGetStudentExamsByExamId() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/getStudentExamsByExamId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"examId\": 10}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(Constants.NOT_AUTHORIZED_STRING_EXAM));
        }
    }

    @Test
    void getStudentExamsByExamIdNoEntity() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/getStudentExamsByExamId")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content("{ \"examId\": -1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(containsString(Constants.NOT_FOUND)));
        }
    }

    @Test
    void deleteStudentExamById() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            mockMvc.perform(post("/exam_service/deleteStudentExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.ID_1_EXAM)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(content().string(containsString(Constants.DELETION)));
        }
    }

    @Test
    void notAuthorizedExceptionDeleteStudent() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/deleteStudentExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void createStudentExam() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() - 100000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() + 100000L));
        doReturn(studentExam3).when(studentExamRepository).save(any(StudentExam.class));


        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {

                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.USER_1_COURSE_ID_5,
                        Constants.GET_ENROLLMENT, ""))
                    .thenReturn(Constants.ENROLLMENT_STRING);

                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                        Constants.EXAM_QUESTION_BY_COURSE,
                        "")).thenReturn(serializer.serialize(studentExam1));

                mockedCommunication.when(() -> ExamServiceCommunication
                    .postRequest(Constants.USER_ID_1, Constants.GET_EXTRA_TIME,
                        "")).thenReturn(Constants.EXTRA_TIME_10);

                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(Constants.EXAM_QUESTION)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content()
                        .string(containsString(serializer.serialize(studentExam3))));
            }
        }
    }

    @Test
    void createStudentExamMoreThan3() throws Exception {
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                        Constants.EXAM_QUESTION_BY_COURSE,
                        "")).thenReturn(serializer.serialize(studentExam1));
                mockedCommunication.when(() -> ExamServiceCommunication
                    .postRequest(Constants.USER_ID_1, Constants.GET_EXTRA_TIME,
                        "")).thenReturn(Constants.EXTRA_TIME_10);
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.USER_2_COURSE_ID_5,
                        Constants.GET_ENROLLMENT, ""))
                    .thenReturn(Constants.ENROLLMENT_STRING);
                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content("{\"examId\":2,\"userId\":\"2\",\"id\":1,\"selected\":false}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(403))
                    .andExpect(content()
                        .string(containsString("The maximum amount of tries has been reached.")));
            }
        }
    }

    @Test
    void createStudentExamTimeExceptionBefore() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() - 100000000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() - 100000000L));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                        Constants.EXAM_QUESTION_BY_COURSE,
                        "")).thenReturn(serializer.serialize(studentExam1));
                mockedCommunication.when(() -> ExamServiceCommunication
                    .postRequest(Constants.USER_ID_1, Constants.GET_EXTRA_TIME,
                        "")).thenReturn(Constants.EXTRA_TIME_10);
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.USER_1_COURSE_ID_5,
                        Constants.GET_ENROLLMENT, ""))
                    .thenReturn(Constants.ENROLLMENT_STRING);
                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(Constants.EXAM_QUESTION)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(403))
                    .andExpect(content()
                        .string(containsString("The exam is not available at this time.")));
            }
        }
    }

    @Test
    void createStudentExamTimeExceptionAfter() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() + 100000000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() + 100000000L));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                        Constants.EXAM_QUESTION_BY_COURSE,
                        "")).thenReturn(serializer.serialize(studentExam1));
                mockedCommunication.when(() -> ExamServiceCommunication
                    .postRequest(Constants.USER_ID_1, Constants.GET_EXTRA_TIME,
                        "")).thenReturn(Constants.EXTRA_TIME_10);
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.USER_1_COURSE_ID_5,
                        Constants.GET_ENROLLMENT, ""))
                    .thenReturn(Constants.ENROLLMENT_STRING);
                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(Constants.EXAM_QUESTION)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(403))
                    .andExpect(content()
                        .string(containsString("The exam is not available at this time.")));
            }
        }
    }

    @Test
    void createStudentExamNotFound() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() - 1000000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() + 100000L));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.USER_1_COURSE_ID_5,
                        Constants.GET_ENROLLMENT, ""))
                    .thenReturn(Constants.ENROLLMENT_STRING);
                mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(Constants.EXAM_QUESTION)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(404))
                    .andExpect(
                        content().string(containsString("Exam not found")));
            }
        }
    }

    @Test
    void createStudentExamNoUser() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() - 100000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() + 100000L));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                        Constants.EXAM_QUESTION_BY_COURSE,
                        "")).thenReturn(serializer.serialize(studentExam1));
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.USER_1_COURSE_ID_5,
                        Constants.GET_ENROLLMENT, ""))
                    .thenReturn(Constants.ENROLLMENT_STRING);
                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(Constants.EXAM_QUESTION)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(404))
                    .andExpect(content().string(containsString("User not found")));
            }
        }
    }

    @Test
    void createStudentExamNotEnrolled() throws Exception {
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedCommunication.when(() -> ExamServiceCommunication.getRequest(
                    Constants.GET_ENROLLMENT + exam1.getCourseId()
                        + "/" + "1", ""))
                    .thenReturn("{}");
                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                exam1.setStart(new Timestamp(0));
                exam1.setEnd(new Timestamp(System.currentTimeMillis() + 1000000));
                mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(Constants.EXAM_QUESTION)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(403))
                    .andExpect(
                        content()
                            .string(containsString("The user is not enrolled in this course.")));
            }
        }
    }

    @Test
    void createStudentExamNotEnrolledNull() throws Exception {
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            exam1.setStart(new Timestamp(0));
            exam1.setEnd(new Timestamp(System.currentTimeMillis() + 1000000));
            mockMvc.perform(post(Constants.CREATE_STUDENT_EXAM_STRING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.EXAM_QUESTION)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(
                    content()
                        .string(containsString("The user is not enrolled in this course.")));
        }
    }


    @Test
    void notAuthorizedExceptionCreateStudentExam() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post("/exam_service/createStudentExam")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void notAuthorizedExceptionSubmitStudentExam() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post(Constants.SUBMIT_EXAM)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void submitStudentExam() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() - 100000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() + 100000L));
        doReturn(studentExam4).when(studentExamRepository).save(any(StudentExam.class));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                        Constants.EXAM_QUESTION_BY_COURSE,
                        "")).thenReturn(serializer.serialize(studentExam1));

                mockedCommunication.when(() -> ExamServiceCommunication
                    .postRequest("{\"questionIds\":[1,2]}",
                        "8082/course_service/getQuestionsById", ""))
                    .thenReturn(questionListJson.toString());

                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockMvc.perform(post(Constants.SUBMIT_EXAM)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(serializer.serialize(studentExam3))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(
                        content().string(containsString(serializer.serialize(studentExam4))));
            }
        }
    }

    @Test
    void submitStudentExamNull() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() - 100000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() + 100000L));
        doReturn(studentExam4).when(studentExamRepository).save(any(StudentExam.class));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                        Constants.EXAM_QUESTION_BY_COURSE,
                        "")).thenReturn(serializer.serialize(studentExam1));
                mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                    .thenReturn(true);
                mockMvc.perform(post(Constants.SUBMIT_EXAM)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(serializer.serialize(studentExam3))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(404))
                    .andExpect(content().string("Could not retrieve correct answers."));
            }
        }
    }

    @Test
    void submitStudentExamAfterEnd() throws Exception {
        exam1.setStart(new Timestamp(System.currentTimeMillis() - 100000L));
        exam1.setEnd(new Timestamp(System.currentTimeMillis() + 100000L));
        studentExam3.setStartingTime(new Timestamp(System.currentTimeMillis() - 10000000L));
        doReturn(studentExam4).when(studentExamRepository).save(any(StudentExam.class));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            mockedAuth.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(true);
            mockMvc.perform(post(Constants.SUBMIT_EXAM)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(studentExam3))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString("The exam is over.")));
        }
    }

    @Test
    void notAuthorizedExceptionGetLeastAnsweredQuestions() throws Exception {
        try (MockedStatic<Authorisation> mockedStatic = Mockito.mockStatic(Authorisation.class)) {
            mockedStatic.when(() -> Authorisation.getAuthorisation("", 0))
                .thenReturn(false);
            mockMvc.perform(post(Constants.GET_LEAST_ANSWERED)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(Constants.COURSE_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
                .andExpect(content().string(containsString(Constants.NOT_AUTHORIZED_STRING_EXAM)));
        }
    }

    @Test
    void notEnoughQuestionsGetLeastAnsweredQuestions() throws Exception {
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            mockedAuth.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            when(studentExamRepository.findByExamId(1))
                .thenReturn(List.of());
            mockMvc.perform(post(Constants.GET_LEAST_ANSWERED)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(studentExam3))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(
                    containsString("There was less than 2 incorrectly answered questions.")));
        }
    }

    @Test
    void getLeastAnsweredQuestionsNullTests() throws Exception {
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            mockedAuth.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            ExamQuestion q2 = new ExamQuestion();
            ExamQuestion q3 = new ExamQuestion();
            q2.setCorrect(true);
            q2.setQuestion(2);
            q3.setCorrect(false);
            q3.setQuestion(3);
            ExamQuestion q4 = new ExamQuestion();
            q4.setCorrect(false);
            q4.setQuestion(4);
            StudentExam exam = new StudentExam();
            ExamQuestion q1 = new ExamQuestion();
            exam.setExamQuestions(List.of(q1, q2, q3, q4));
            doReturn(List.of(exam)).when(studentExamRepository).findByExamId(1);

            mockMvc.perform(post(Constants.GET_LEAST_ANSWERED)
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SESSIONHEADERKEY, "")
                .content(serializer.serialize(studentExam3))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(content().string(
                    containsString("Questions could not be retrieved.")));
        }
    }

    @Test
    void getLeastAnsweredQuestions() throws Exception {
        ExamQuestion q1 = new ExamQuestion();
        q1.setCorrect(false);
        q1.setQuestion(1);
        ExamQuestion q2 = new ExamQuestion();
        q2.setCorrect(false);
        q2.setQuestion(2);
        ExamQuestion q3 = new ExamQuestion();
        q3.setCorrect(false);
        q3.setQuestion(3);
        StudentExam exam = new StudentExam();
        exam.setExamQuestions(List.of(q3, q2, q2, q1, q1, q1));
        try (MockedStatic<Authorisation> mockedAuth = Mockito.mockStatic(Authorisation.class)) {
            mockedAuth.when(() -> Authorisation.getAuthorisation("", 1))
                .thenReturn(true);
            when(studentExamRepository.findByExamId(1))
                .thenReturn(List.of(exam));
            try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
                .mockStatic(ExamServiceCommunication.class)) {
                JSONArray a = new JSONArray();
                a.put(1);
                a.put(2);
                JSONObject json = new JSONObject();
                json.put("questionIds", a);
                mockedCommunication
                    .when(() -> ExamServiceCommunication.postRequest(json.toString(),
                        Constants.GET_QUESTIONS_BY_ID,
                        "")).thenReturn(questionListJson.toString());

                mockMvc.perform(post(Constants.GET_LEAST_ANSWERED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.SESSIONHEADERKEY, "")
                    .content(serializer.serialize(studentExam3))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(200))
                    .andExpect(content().string(containsString(questionListJson.toString())));
            }
        }
    }

    @Test
    void isExamLimitReachedTest() {
        when(studentExamRepository.findByExamAndUser(1, "1"))
            .thenReturn(List.of(new StudentExam(), new StudentExam(), new StudentExam()));
        Assertions.assertTrue(support.isExamLimitReached(1, "1"));
        when(studentExamRepository.findByExamAndUser(1, "1"))
            .thenReturn(List.of(new StudentExam(), new StudentExam()));
        Assertions.assertFalse(support.isExamLimitReached(1, "1"));
    }

    @Test
    void isExamTimeCorrectTest() {
        Exam exam = new Exam();
        exam.setStart(new Timestamp(System.currentTimeMillis() - 100000));
        exam.setEnd(new Timestamp(System.currentTimeMillis() + 100000));
        Assertions.assertTrue(support.isExamTimeCorrect(exam));
        exam.setStart(new Timestamp(System.currentTimeMillis() + 100000));
        exam.setEnd(new Timestamp(System.currentTimeMillis() + 200000));
        Assertions.assertFalse(support.isExamTimeCorrect(exam));
        exam.setStart(new Timestamp(System.currentTimeMillis() - 200000));
        exam.setEnd(new Timestamp(System.currentTimeMillis() - 100000));
        Assertions.assertFalse(support.isExamTimeCorrect(exam));
    }

    @Test
    void hasExamEndedTest() {
        int extraTime = 5;
        long time = (extraTime * 60) * 1000 + (20 * 60) * 1000;
        Timestamp t1 = new Timestamp(System.currentTimeMillis() - time - 1000);
        Timestamp t2 = new Timestamp(System.currentTimeMillis() - time + 1000);
        Assertions.assertTrue(support.hasExamEnded(t1, extraTime));
        Assertions.assertFalse(support.hasExamEnded(t2, extraTime));
    }

    @Test
    void getCorrectQuestions() {
        Question q = new Question();
        Answer correct = new Answer();
        correct.setCorrect(true);
        Answer incorrect = new Answer();
        incorrect.setCorrect(false);
        q.setAnswers(
            List.of(
                correct,
                correct,
                incorrect
            ));

        StudentAnswer selected = new StudentAnswer();
        selected.setSelected(true);
        StudentAnswer notselected = new StudentAnswer();
        notselected.setSelected(false);
        //Incorrect answer
        ExamQuestion exQ1 = new ExamQuestion();
        exQ1.setStudentAnswers(
            List.of(
                notselected,
                notselected,
                notselected
            )
        );
        //Incorrect answer
        ExamQuestion exQ2 = new ExamQuestion();
        exQ2.setStudentAnswers(
            List.of(
                selected,
                notselected,
                notselected
            )
        );
        //Correct answer
        ExamQuestion exQ3 = new ExamQuestion();
        exQ3.setStudentAnswers(
            List.of(
                selected,
                selected,
                notselected
            )
        );
        Assertions.assertEquals(1,
            support.countCorrectQuestions(List.of(exQ1, exQ2, exQ3), List.of(q, q, q)));
    }

    @Test
    void studentExamFromCourseServiceTest() {
        try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
            .mockStatic(ExamServiceCommunication.class)) {
            mockedCommunication
                .when(() -> ExamServiceCommunication.postRequest(Constants.COURSE_ID_5,
                    Constants.EXAM_QUESTION_BY_COURSE,
                    "")).thenReturn(serializer.serialize(studentExam1));
            StudentExam s = support.studentExamFromCourseService(5, "");
            Assertions.assertEquals(s, studentExam1);
        }
    }

    @Test
    void studentExamExtraTimeTest() {
        try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
            .mockStatic(ExamServiceCommunication.class)) {
            mockedCommunication
                .when(() -> ExamServiceCommunication.postRequest(Constants.USER_ID_1,
                    Constants.GET_EXTRA_TIME,
                    "")).thenReturn(Constants.EXTRA_TIME_10);

            int extraTimeInt = support.studentExamExtraTime("1", "");
            Assertions.assertEquals(extraTimeInt, 10);
        }
    }

    @Test
    void getCorrectQuestionsFromCourseTest() {
        try (MockedStatic<ExamServiceCommunication> mockedCommunication = Mockito
            .mockStatic(ExamServiceCommunication.class)) {
            JSONArray a = new JSONArray();
            a.put(1);
            a.put(2);
            JSONObject json = new JSONObject();
            json.put("questionIds", a);
            mockedCommunication
                .when(() -> ExamServiceCommunication.postRequest(json.toString(),
                    Constants.GET_QUESTIONS_BY_ID,
                    "")).thenReturn(questionListJson.toString());

            List<Question> l = support.getCorrectQuestionsFromCourse(examQuestions, "");
            Assertions.assertEquals(questionList, l);
        }
    }

    @Test
    void getCorrectQuestionsFromCourseNull() {
        List<Question> l = support.getCorrectQuestionsFromCourse(examQuestions, "");
        Assertions.assertNull(l);
    }
}