package app.controllers;

import app.communication.Authorisation;
import app.communication.ExamServiceCommunication;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Exam;
import app.models.ExamQuestion;
import app.models.Question;
import app.models.StudentExam;
import app.repositories.ExamRepository;
import app.repositories.StudentExamRepository;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("exam_service")
public class StudentExamController {

    private final transient StudentExamRepository studentExamRepository;
    private final transient ExamRepository examRepository;
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();
    private final transient StudentExamSupport support;

    /**
     * Autowired constructor.
     *
     * @param studentExamRepository StudentExamRepository.
     * @param examRepository        ExamRepository.
     */
    @Autowired
    public StudentExamController(StudentExamRepository studentExamRepository,
                                 ExamRepository examRepository) {
        this.studentExamRepository = studentExamRepository;
        this.examRepository = examRepository;
        this.support = new StudentExamSupport(studentExamRepository);
    }

    /**
     * Get StudentExams by UserId.
     *
     * @param data         JSONObject with "UserId" key.
     * @param sessionToken session token.
     * @param jwtToken     jwt.
     * @return JSONObject with StudentExam.
     */
    @PostMapping(path = "studentExamByUserId")
    public ResponseEntity<String> studentExamByUserId(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken,
                                                      @RequestHeader("session") String jwtToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        if (!new JSONObject(data).has(Constants.USER_ID_STRING)) {
            return new ResponseEntity<>(Constants.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Jws<Claims> jwsObject = Jwts.parserBuilder()
            .requireSubject("session")
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(jwtToken);
        if (!jwsObject.getBody().get("netId", String.class)
            .equals(new JSONObject(data).get(Constants.USER_ID_STRING))) {
            throw new SecurityException();
        }

        List<StudentExam> studentExams =
            studentExamRepository
                .findByUser((String) new JSONObject(data).get(Constants.USER_ID_STRING));

        JSONArray jsonArray = new JSONArray();
        for (StudentExam studentExam : studentExams) {
            jsonArray.put(serializer.serialize(studentExam));
        }

        JSONObject result = new JSONObject();
        result.put("studentExamList", jsonArray);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    /**
     * Get StudentExam by id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return JSONObject with StudentExam.
     */
    @PostMapping(path = "studentExamById")
    public ResponseEntity<String> studentExamById(@RequestBody String data,
                                                  @RequestHeader(Constants.SESSIONHEADERKEY)
                                                      String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject jsonObject = new JSONObject(data);
        StudentExam studentExam =
            studentExamRepository.findById((int) jsonObject.get("id"));
        return (studentExam != null)
            ? new ResponseEntity<>(serializer.serialize(studentExam), HttpStatus.OK) :
            new ResponseEntity<>(Constants.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    /**
     * Get List of StudentExam by exam id.
     *
     * @param data         JSONObject with "examId" key.
     * @param sessionToken session token.
     * @return JSONObject with "studentExamList" key and JSONArray of StudentExam as value.
     */
    @PostMapping(path = "getStudentExamsByExamId")
    public ResponseEntity<String> getStudentExamsByExamId(@RequestBody String data,
                                                          @RequestHeader(Constants.SESSIONHEADERKEY)
                                                              String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            throw new SecurityException();
        }

        List<StudentExam> studentExams =
            studentExamRepository.findByExamId((int) new JSONObject(data).get("examId"));

        JSONArray jsonArray = new JSONArray();
        for (StudentExam s : studentExams) {
            jsonArray.put(serializer.serialize(s));
        }
        JSONObject studentAnswerJson = new JSONObject();
        studentAnswerJson.put("studentExamList", jsonArray);
        return studentExams.size() == 0
            ? new ResponseEntity<>(Constants.NOT_FOUND, HttpStatus.NOT_FOUND) :
            new ResponseEntity<>(studentAnswerJson.toString(), HttpStatus.OK);
    }

    /**
     * Delete StudentExam by id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return "Success" if deletion was successful.
     */
    @PostMapping(path = "deleteStudentExam")
    public ResponseEntity<String> deleteStudentExam(@RequestBody String data,
                                                    @RequestHeader(Constants.SESSIONHEADERKEY)
                                                        String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            throw new SecurityException();
        }
        JSONObject jsonObject = new JSONObject(data);
        studentExamRepository.deleteById((int) jsonObject.get("id"));
        return new ResponseEntity<>("Deletion was successful", HttpStatus.OK);
    }

    /**
     * Generate StudentExam with random questions based on user and exam ids.
     *
     * @param data         JSONObject with "examId" and "userId" keys.
     * @param sessionToken session token.
     * @return JSONObject with StudentExam.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @PostMapping(path = "createStudentExam")
    public ResponseEntity<String> createStudentExam(@RequestBody String data,
                                                    @RequestHeader(Constants.SESSIONHEADERKEY)
                                                        String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        int exId = (int) json.get(Constants.EXAM_ID);
        String userId = (String) json.get(Constants.USER_ID_STRING);
        Exam exam = examRepository.findById(exId);
        String userAndCourseId =
            "{\"userId\":" + userId + ",\"courseId\":" + exam.getCourseId() + "}";
        // Check if user is enrolled
        String enrollmentString =
            ExamServiceCommunication
                .postRequest(userAndCourseId,
                    "8082/course_service/getEnrollmentByCourseAndUser/",
                    sessionToken);

        if (enrollmentString == null || !new JSONObject(enrollmentString).has("id")) {
            return new ResponseEntity<>("The user is not enrolled in this course.",
                HttpStatus.FORBIDDEN);
        }
        // Check whether exam was already taken 3 times
        if (support.isExamLimitReached(exId, userId)) {
            return new ResponseEntity<>("The maximum amount of tries has been reached.",
                HttpStatus.FORBIDDEN);
        }
        //Check whether deadline passed or exam hasn't started yet
        if (!support.isExamTimeCorrect(exam)) {
            return new ResponseEntity<>("The exam is not available at this time.",
                HttpStatus.FORBIDDEN);
        }
        //Request course-service to create StudentExam
        StudentExam s = support.studentExamFromCourseService(exam.getCourseId(), sessionToken);
        if (s == null) {
            return new ResponseEntity<>("Exam not found", HttpStatus.NOT_FOUND);
        }
        //Ask for extra time
        Integer extraTime = support.studentExamExtraTime(userId, sessionToken);
        if (extraTime == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        //Setup StudentExam
        s.setGrade(0F);
        s.setCorrectQuestions(0);
        s.setExamId(exId);
        s.setStartingTime(new Timestamp(System.currentTimeMillis()));
        s.setExtraTime(extraTime);
        s.setUser(userId);
        StudentExam studentExam = studentExamRepository.save(s);
        return new ResponseEntity<>(serializer.serialize(studentExam), HttpStatus.OK);
    }

    /**
     * Submits StudentExam and saves it to database, if time is correct.
     *
     * @param data         JSONObject with StudentExam.
     * @param sessionToken session token.
     * @return JSONObject with StudentExam if successfully submitted, else error message.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @PostMapping(path = "submitStudentExam")
    public ResponseEntity<String> submitStudentExam(@RequestBody String data,
                                                    @RequestHeader(Constants.SESSIONHEADERKEY)
                                                        String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        StudentExam studentExam = (StudentExam) serializer.deserialize(data, StudentExam.class);
        //Check if exam has ended
        if (support.hasExamEnded(studentExam.getStartingTime(), studentExam.getExtraTime())) {
            return new ResponseEntity<>("The exam is over.",
                HttpStatus.FORBIDDEN);
        }
        //Request questions with correct answers from course service
        List<ExamQuestion> studentQuestions = studentExam.getExamQuestions();
        List<Question> correctQuestions =
            support.getCorrectQuestionsFromCourse(studentQuestions, sessionToken);
        if (correctQuestions == null) {
            return new ResponseEntity<>("Could not retrieve correct answers.",
                HttpStatus.NOT_FOUND);
        }

        //Calculate grade and number of correctly answered questions
        int correctAnswers = support.countCorrectQuestions(studentQuestions, correctQuestions);
        float grade = (float) correctAnswers / studentQuestions.size() * 9 + 1;
        //Set fields and save to database
        studentExam.setExamQuestions(studentQuestions);
        studentExam.setCorrectQuestions(correctAnswers);
        studentExam.setGrade(grade);
        StudentExam e = studentExamRepository.save(studentExam);
        return new ResponseEntity<>(serializer.serialize(e), HttpStatus.OK);
    }

    /**
     * Return 2 least answered questions based on exam id.
     *
     * @param data         JSONObject with "examId" key.
     * @param sessionToken session token.
     * @return JSONObject with "questions" key and JSONArray of Question as value.
     */
    @PostMapping("getLeastAnsweredQuestions")
    public ResponseEntity<String> getLeastAnsweredQuestions(
        @RequestBody String data, @RequestHeader(Constants.SESSIONHEADERKEY)
                                                                String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        int id = (int) json.get("examId");
        List<StudentExam> exams = studentExamRepository.findByExamId(id);
        // Counting least answered questions
        Set<Map.Entry<Integer, Long>> set = exams.stream()
            .flatMap(x -> x.getExamQuestions().stream())
            .filter(x -> x.getCorrect() != null && !x.getCorrect())
            .map(ExamQuestion::getQuestion)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet();
        List<Map.Entry<Integer, Long>> questions = new ArrayList<>(set);
        int two = 2;
        if (questions.size() < two) {
            return new ResponseEntity<>("There was less than 2 incorrectly answered questions.",
                HttpStatus.NOT_FOUND);
        }
        // Sorting in descending order and retrieving first two
        questions.sort(Comparator.comparing(x -> -x.getValue()));

        // Retrieving questions from course service
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(questions.get(0).getKey());
        jsonArray.put(questions.get(1).getKey());
        jsonObject.put("questionIds", jsonArray);

        String response =
            ExamServiceCommunication
                .postRequest(jsonObject.toString(), "8082/course_service/getQuestionsById",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity<>("Questions could not be retrieved.",
                HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}