package app.controllers;

import app.communication.StudentServiceCommunication;
import app.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handler for http requests to the exam service concerning student exams.
 */
@RestController
@RequestMapping("student_service")
public class StudentExamController {

    /**
     * Queries the exam service for a particular student exam.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested student exam as json object.
     */
    @PostMapping("studentExamById")
    @ResponseBody
    public ResponseEntity<String> studentExamById(@RequestBody String data,
                                                  @RequestHeader(Constants.SESSIONHEADERKEY)
                                                      String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/studentExamById", sessionToken);
        if (response == null) {
            return new ResponseEntity("Student exam not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Get all student exams for certain user.
     *
     * @param data         JSONObject (as String) with "UserId" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested student exam as json object.
     */
    @PostMapping("studentExamByUserId")
    @ResponseBody
    public ResponseEntity<String> studentExamByUserId(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/studentExamByUserId", sessionToken);
        if (response == null) {
            return new ResponseEntity("Student exams not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the exam service to create a new student exam.
     *
     * @param data         JSONObject (as String) with "examId" and "userId" keys.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested student exam as json object.
     */
    @PostMapping("createStudentExam")
    @ResponseBody
    public ResponseEntity<String> createStudentExam(@RequestBody String data,
                                                    @RequestHeader(Constants.SESSIONHEADERKEY)
                                                        String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/createStudentExam", sessionToken);
        if (response == null) {
            return new ResponseEntity("Exam creation unsuccessful", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the exam service to save/submit a student exam.
     *
     * @param data         JSONObject (as String) with StudentExam.
     * @param sessionToken A valid token received from the authorization service.
     * @return The submitted student exam as json object.
     */
    @PostMapping("submitStudentExam")
    @ResponseBody
    public ResponseEntity<String> submitStudentExam(@RequestBody String data,
                                                    @RequestHeader(Constants.SESSIONHEADERKEY)
                                                        String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/submitStudentExam", sessionToken);
        if (response == null) {
            return new ResponseEntity("Submitting exam unsuccessful", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
