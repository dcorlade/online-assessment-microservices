package app.controllers;

import app.communication.TeacherServiceCommunication;
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
 * Handler for http requests to the course service concerning question administration (CRUD).
 */
@RestController
@RequestMapping("teacher_service")
public class QuestionController {

    /**
     * Queries the course service for a particular question.
     *
     * @param data         JSON String with "id" as key and a QuestionId as a value.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested question as a JSON object.
     */
    @PostMapping("questionById")
    @ResponseBody
    public ResponseEntity<String> questionById(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data,
                    "8082/courseService/question/getQuestion",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to update a question.
     *
     * @param data         Takes a String representation of an Question to overwrite.
     *                     This data has to be supplied in the body of the HTTP-request
     *                     in JSON format.
     * @param sessionToken A valid token received from the authorization service.
     * @return The updated question as a JSON object.
     */
    @PostMapping("updateQuestion")
    @ResponseBody
    public ResponseEntity<String> updateQuestion(@RequestBody String data,
                                                 @RequestHeader(Constants.SESSIONHEADERKEY)
                                                     String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data,
                    "8082/courseService/question/updateQuestion",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to update", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to create a new question.
     *
     * @param data         The http request body.
     * @param sessionToken A valid token received from the authorization service.
     * @return The newly created question as a JSON object.
     */
    @PostMapping("createQuestion")
    @ResponseBody
    public ResponseEntity<String> createQuestion(@RequestBody String data,
                                                 @RequestHeader(Constants.SESSIONHEADERKEY)
                                                     String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data,
                    "8082/courseService/question/createQuestion",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to create", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to delete a question.
     *
     * @param data         JSON String with "id" as key and a questionId as a value.
     * @param sessionToken A valid token received from the authorization service.
     * @return The response.
     */
    @PostMapping("deleteQuestion")
    @ResponseBody
    public ResponseEntity<String> deleteQuestion(@RequestBody String data,
                                                 @RequestHeader(Constants.SESSIONHEADERKEY)
                                                     String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data,
                    "8082/courseService/question/deleteQuestion",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}
