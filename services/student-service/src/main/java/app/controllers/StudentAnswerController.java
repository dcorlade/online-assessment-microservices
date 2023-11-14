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
 * Handler for http requests to the exam service concerning student answers.
 */
@RestController
@RequestMapping("student_service")
public class StudentAnswerController {

    /**
     * Queries the exam service for a particular student answer.
     *
     * @param data         JSONObject (as String) with "id" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested student answer as json object.
     */
    @PostMapping("getStudentAnswer")
    @ResponseBody
    public ResponseEntity<String> getStudentAnswer(@RequestBody String data,
                                                   @RequestHeader(Constants.SESSIONHEADERKEY)
                                                       String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/studentAnswerById", sessionToken);
        if (response == null) {
            return new ResponseEntity(Constants.STUDENT_ANSWER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Queries the exam service for a list of student answers, given an exam question.
     *
     * @param data         JSONObject (as String) with "examQuestionId" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested list of student answers as a JSONArray wrapped in a JSONObject.
     */
    @PostMapping("getStudentAnswersByExamQuestionId")
    @ResponseBody
    public ResponseEntity<String> getStudentAnswersByExamQuestionId(
        @RequestBody String data, @RequestHeader(Constants.SESSIONHEADERKEY)
                                                                        String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/studentAnswerByExamQuestionId", sessionToken);
        if (response == null) {
            return new ResponseEntity("Exam Question ID not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }


    /**
     * Requests the exam service to update a particular student answer.
     *
     * @param data         JSONObject (as String) with StudentAnswer and "studentExamId" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The updated student answer as json object.
     */
    @PostMapping("updateStudentAnswer")
    @ResponseBody
    public ResponseEntity<String> updateStudentAnswer(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/updateStudentAnswer", sessionToken);
        if (response == null) {
            return new ResponseEntity(Constants.STUDENT_ANSWER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
