package app.controllers;

import app.communication.StudentServiceCommunication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handler for http requests to the exam service.
 */
@RestController
@RequestMapping("student_service")
public class ExamController {

    private final transient String headerSessionKey = "session";

    /**
     * Queries the exam service for an exam entry.
     *
     * @param data         JSONObject (as String) with "id" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested exam as a JSON object.
     */
    @PostMapping("examById")
    @ResponseBody
    public ResponseEntity<String> examById(@RequestBody String data,
                                           @RequestHeader(headerSessionKey) String sessionToken) {
        String response =
            StudentServiceCommunication
                .postRequest(data, "8083/exam_service/examById", sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
