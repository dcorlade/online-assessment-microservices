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
 * Handler for http requests to the exam service concerning exam questions.
 */
@RestController
@RequestMapping("student_service")
public class ExamQuestionsController {


    /**
     * Queries the exam service for a list of exam questions,
     * given the logged in student and a student exam ID.
     *
     * @param data         JSONObject (as String) with "id" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested list of questions as a JSONArray wrapped in a JSONObject.
     */
    @PostMapping("getExamQuestions")
    @ResponseBody
    public ResponseEntity<String> getExamQuestions(@RequestBody String data,
                                                   @RequestHeader(Constants.SESSIONHEADERKEY)
                                                       String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/examQuestionsByStudentExamId",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Error fetching exam questions", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
