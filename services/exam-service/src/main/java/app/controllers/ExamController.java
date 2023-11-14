package app.controllers;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Exam;
import app.models.StudentExam;
import app.repositories.ExamRepository;
import app.repositories.StudentExamRepository;
import app.serializerfactory.Serializer;
import java.util.List;
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
public class ExamController {

    private final transient ExamRepository examRepository;
    private final transient StudentExamRepository studentExamRepository;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();


    /**
     * Autowired constructor.
     *
     * @param examRepository        ExamRepository.
     * @param studentExamRepository StudentExamRepository.
     */
    @Autowired
    public ExamController(ExamRepository examRepository,
                          StudentExamRepository studentExamRepository) {
        this.examRepository = examRepository;
        this.studentExamRepository = studentExamRepository;
    }

    /**
     * Get exam by id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return JSONObject with exam, if found. Else "Entity not found".
     */
    @PostMapping("examById")
    public ResponseEntity<String> examById(@RequestBody String data,
                                           @RequestHeader(Constants.SESSIONHEADERKEY)
                                               String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        Exam exam = examRepository.findById((int) json.get("id"));
        if (exam == null) {
            return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serializer.serialize(exam), HttpStatus.OK);
    }

    /**
     * Updates exam entity in database.
     *
     * @param data         JSONObject with exam entity.
     * @param sessionToken session token.
     * @return JSONObject with exam entity.
     */
    @PostMapping("updateExam")
    public ResponseEntity<String> updateExam(@RequestBody String data,
                                             @RequestHeader(Constants.SESSIONHEADERKEY)
                                                 String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        Exam exam = (Exam) serializer.deserialize(json.toString(), Exam.class);
        exam = examRepository.save(exam);
        return new ResponseEntity<>(serializer.serialize(exam), HttpStatus.OK);
    }

    /**
     * Delete exam by id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return "Success" if deletion successful.
     */
    @PostMapping("deleteExam")
    public ResponseEntity<String> deleteExam(@RequestBody String data,
                                             @RequestHeader(Constants.SESSIONHEADERKEY)
                                                 String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        int id = (int) json.get("id");
        examRepository.deleteById(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    /**
     * Get average grade by exam along all students.
     *
     * @param data         JSONObject with "examId" key.
     * @param sessionToken session token.
     * @return JSONObject with "avgGrade" entry.
     */
    @PostMapping("getAverageGrade")
    public ResponseEntity<String> getAverageGrade(@RequestBody String data,
                                                  @RequestHeader(Constants.SESSIONHEADERKEY)
                                                      String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        int examId = (int) json.get("examId");
        List<StudentExam> exams = studentExamRepository.findByExamId(examId);
        float avg = (float) exams.stream()
            .filter(x -> x.getGrade() > 0)
            .mapToDouble(StudentExam::getGrade)
            .average()
            .orElse(0);
        JSONObject res = new JSONObject();
        res.put("avgGrade", avg);
        return new ResponseEntity<>(res.toString(), HttpStatus.OK);
    }

    /**
     * Get the total amount of student that have taken an assessment.
     *
     * @param data         JSONObject with "examId" key.
     * @param sessionToken session token.
     * @return JSONObject with "amount" entry.
     */
    @PostMapping("getAmountOfStudents")
    public ResponseEntity<String> getAmountOfStudents(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            throw new SecurityException();
        }
        int examId = new JSONObject(data).getInt("examId");
        if (examRepository.findById(examId) == null) {
            return new ResponseEntity<>("There exists no exam with that id", HttpStatus.NOT_FOUND);
        }
        List<StudentExam> studentExams = studentExamRepository.findByExamId(examId);
        Object[] uniqueUsers = studentExams.stream()
            .map(StudentExam::getUser)
            .distinct().toArray();
        return new ResponseEntity<>(
            new JSONObject().put("amount", uniqueUsers.length).toString(),
            HttpStatus.OK);
    }

}
