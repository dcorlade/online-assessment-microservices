package app.controllers;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.StudentAnswer;
import app.models.StudentExam;
import app.repositories.StudentAnswerRepository;
import app.repositories.StudentExamRepository;
import app.serializerfactory.Serializer;
import java.util.List;
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
public class StudentAnswerController {

    private final transient StudentAnswerRepository studentAnswerRepository;
    private final transient StudentExamRepository studentExamRepository;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    private final transient StudentExamSupport support;

    /**
     * Autowired constructor.
     *
     * @param studentAnswerRepository StudentAnswerRepository.
     * @param studentExamRepository   StudentExamRepository.
     */
    @Autowired
    public StudentAnswerController(StudentAnswerRepository studentAnswerRepository,
                                   StudentExamRepository studentExamRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
        this.studentExamRepository = studentExamRepository;
        this.support = new StudentExamSupport(studentExamRepository);
    }

    /**
     * Get StudentAnswer by id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return JSONObject with StudentAnswer.
     */
    @PostMapping(path = "studentAnswerById")
    public ResponseEntity<String> studentAnswerById(@RequestBody String data,
                                                    @RequestHeader(Constants.SESSIONHEADERKEY)
                                                        String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject jsonObject = new JSONObject(data);
        StudentAnswer studentAnswer = studentAnswerRepository.findById((int) jsonObject.get("id"));
        return (studentAnswer != null)
            ? new ResponseEntity<>(serializer.serialize(studentAnswer), HttpStatus.OK) :
            new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Get List of StudentAnswer by exam question id.
     *
     * @param data         JSONObject with "examQuestionId" key.
     * @param sessionToken session token.
     * @return JSONObject with "studentAnswerList" key and JSONArray of StudentAnswer as value.
     */
    @PostMapping(path = "studentAnswerByExamQuestionId")
    public ResponseEntity<String> studentAnswerByExamQuestionId(
        @RequestBody String data, @RequestHeader(Constants.SESSIONHEADERKEY)
                                                                    String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject jsonObject = new JSONObject(data);
        List<StudentAnswer> studentAnswerList =
            studentAnswerRepository.findByExamQuestion((int) jsonObject.get("examQuestionId"));
        JSONArray jsonArray = new JSONArray();
        for (StudentAnswer s : studentAnswerList) {
            jsonArray.put(serializer.serialize(s));
        }
        JSONObject studentAnswerJson = new JSONObject();
        studentAnswerJson.put("studentAnswerList", jsonArray);

        return new ResponseEntity<>(studentAnswerJson.toString(), HttpStatus.OK);
    }

    /**
     * Delete StudentAnswer by id. Never called by student/exam service.
     *
     * @param data         JSONObject with "id" key specified.
     * @param sessionToken session token.
     * @return "Success" if deletion successful.
     */
    @PostMapping(path = "deleteStudentAnswer")
    public ResponseEntity<String> deleteStudentAnswer(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject jsonObject = new JSONObject(data);
        studentAnswerRepository.deleteById((int) jsonObject.get("id"));
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * Saves StudentAnswer entity to database. Never called by student/exam service.
     *
     * @param data         JSONObject with StudentAnswer.
     * @param sessionToken session token.
     * @return JSONObject with StudentAnswer if saved.
     */
    @PostMapping(path = "createStudentAnswer")
    public ResponseEntity<String> createStudentAnswer(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject jsonObject = new JSONObject(data);
        StudentAnswer s =
            (StudentAnswer) serializer.deserialize(jsonObject.toString(), StudentAnswer.class);
        s = studentAnswerRepository.save(s);
        return new ResponseEntity<>(serializer.serialize(s), HttpStatus.OK);
    }

    /**
     * Update and save StudentAnswer database if exam has not ended yet.
     *
     * @param data         JSONObject with StudentAnswer and "studentExamId" key.
     * @param sessionToken session token.
     * @return JSONObject if StudentAnswer saved.
     */
    @PostMapping(path = "updateStudentAnswer")
    public ResponseEntity<String> updateStudentAnswer(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject jsonObject = new JSONObject(data);
        int studentExamId = (int) jsonObject.get("studentExamId");

        StudentExam studentExam = studentExamRepository.findById(studentExamId);

        //Tests if the exam is over
        if (support.hasExamEnded(studentExam.getStartingTime(), studentExam.getExtraTime())) {
            return new ResponseEntity<>("The exam is over.",
                HttpStatus.FORBIDDEN);
        }
        StudentAnswer s = studentAnswerRepository
            .save((StudentAnswer) serializer.deserialize(data, StudentAnswer.class));
        return new ResponseEntity<>(serializer.serialize(s), HttpStatus.OK);
    }
}
