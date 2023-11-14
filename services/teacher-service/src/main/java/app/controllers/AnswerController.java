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
 * Handler for http requests to the course service concerning answer administration (CRUD).
 */
@RestController
@RequestMapping("teacher_service")
public class AnswerController {

    /**
     * Queries the course service for a particular answer.
     *
     * @param data         JSON-String with "id" key and a AnswerId as a value.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested answer as a JSON object.
     */
    @PostMapping("answerById")
    @ResponseBody
    public ResponseEntity<String> answerById(@RequestBody String data,
                                             @RequestHeader(Constants.SESSIONHEADERKEY)
                                                 String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8082/courseService/answer/getAnswer", sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Queries the course service for all answers belonging to a question.
     *
     * @param data         JSON-String with "id" key and a QuestionId as a value.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested list of answers as a JSONArray wrapped in a JSON object.
     */
    @PostMapping("getAnswerByQuestionId")
    @ResponseBody
    public ResponseEntity<String> getAnswerByQuestionId(@RequestBody String data,
                                                        @RequestHeader(Constants.SESSIONHEADERKEY)
                                                            String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data,
                    "8082/courseService/answer/getAnswerByQuestionId",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Question ID not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to update a particular answer.
     *
     * @param data         Takes a String representation of an Answer to overwrite.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken A valid token received from the authorization service.
     * @return The updated answer as a JSON object.
     */
    @PostMapping("updateAnswer")
    @ResponseBody
    public ResponseEntity<String> updateAnswer(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8082/courseService/answer/updateAnswer", sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to update", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to create a particular answer.
     *
     * @param data         The http request body.
     * @param sessionToken A valid token received from the authorization service.
     * @return The newly created answer as a JSON object.
     */
    @PostMapping("createAnswer")
    @ResponseBody
    public ResponseEntity<String> createAnswer(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data,
                    "8082/courseService/answer/createAnswer",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to create", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to delete a particular answer.
     *
     * @param data         JSON-String with a key 'id' and as value an AnswerId.
     * @param sessionToken A valid token received from the authorization service.
     * @return The response.
     */
    @PostMapping("deleteAnswer")
    @ResponseBody
    public ResponseEntity<String> deleteAnswer(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data,
                    "8082/courseService/answer/deleteAnswer",
                    sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Success", HttpStatus.OK);
    }
}
