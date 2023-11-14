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
 * Handler for http requests to the course service concerning topic administration (CRUD).
 */
@RestController
@RequestMapping("teacher_service")
public class TopicController {

    /**
     * Requests the course service to create a new topic.
     *
     * @param data         Takes a String representation of a Topic to add.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken A valid token received from the authorization service.
     * @return The newly created topic as a JSON object.
     */
    @PostMapping("createTopic")
    @ResponseBody
    public ResponseEntity<String> createTopic(@RequestBody String data,
                                              @RequestHeader(Constants.SESSIONHEADERKEY)
                                                  String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/topic/createTopic", sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to create topic", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to update a topic.
     *
     * @param data         Takes a String representation of a Topic to overwrite.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken A valid token received from the authorization service.
     * @return The updated topic as a JSON object.
     */
    @PostMapping("updateTopic")
    @ResponseBody
    public ResponseEntity<String> updateTopic(@RequestBody String data,
                                              @RequestHeader(Constants.SESSIONHEADERKEY)
                                                  String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/topic/updateTopic",
            sessionToken);
        if (response == null) {
            return new ResponseEntity<>("Failed to update topic", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to delete a topic.
     *
     * @param data         Json String with "id" as key and a topicId as value.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken A valid token received from the authorization service.
     * @return The response.
     */
    @PostMapping("deleteTopic")
    @ResponseBody
    public ResponseEntity<String> deleteTopic(@RequestBody String data,
                                              @RequestHeader(Constants.SESSIONHEADERKEY)
                                                  String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/topic/deleteTopic", sessionToken);
        if (response == null) {
            return new ResponseEntity("Topic not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Success!", HttpStatus.OK);
    }

    /**
     * Queries the course service for a particular topic.
     *
     * @param data         Json String with "id" as key and a topicId as value.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested topic as a JSON object.
     */
    @PostMapping("topicById")
    @ResponseBody
    public ResponseEntity<String> topicById(@RequestBody String data,
                                            @RequestHeader(Constants.SESSIONHEADERKEY)
                                                String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/topic/getTopic", sessionToken);
        if (response == null) {
            return new ResponseEntity("Topic not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
