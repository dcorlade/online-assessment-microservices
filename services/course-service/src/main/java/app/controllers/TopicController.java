package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Topic;
import app.serializerfactory.Serializer;
import app.services.TopicService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courseService/topic")
public class TopicController {
    private final transient TopicService topicService;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    /**
     * Constructs a TopicController.
     *
     * @param topicService Takes an topicService to assign to its instance variable. The constructor
     *                     is Autowired, meaning that Spring will take the instance that is has and
     *                     assign it to the variable automatically.
     */
    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of retrieving a topic from the database.
     *
     * @param data         Json String with "id" as key and a topicId as value.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the topic in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getTopic")
    public ResponseEntity<String> getTopic(@RequestBody String data,
                                           @RequestHeader(Constants.SESSIONHEADERKEY)
                                               String sessionToken) {
        return topicService.getTopic(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of retrieving a topic from the database.
     *
     * @param data         Json String with "id" as key and a courseId as value.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the topics
     *     related to the provided courseId in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getTopicsByCourseId")
    public ResponseEntity<String> getTopicsByCourseId(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        return topicService.getTopicsByCourse(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of adding a topic to the database.
     *
     * @param data         Takes a String representation of a Topic to add.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/createTopic")
    public ResponseEntity<String> addTopic(@RequestBody String data,
                                           @RequestHeader(Constants.SESSIONHEADERKEY)
                                               String sessionToken) {
        return topicService
            .addTopic((Topic) serializer.deserialize(data, Topic.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of updating a topic in the database.
     *
     * @param data         Takes a String representation of a Topic to overwrite.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/updateTopic")
    public ResponseEntity<String> updateTopic(@RequestBody String data,
                                              @RequestHeader(Constants.SESSIONHEADERKEY)
                                                  String sessionToken) {
        return topicService
            .updateTopic((Topic) serializer.deserialize(data, Topic.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of deleting a topic from the database.
     *
     * @param data         Json String with "id" as key and a topicId as value.
     *                     This data has to be supplied in the body of
     *                     the HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     */
    @PostMapping("/deleteTopic")
    public void deleteAnswer(@RequestBody String data,
                             @RequestHeader(Constants.SESSIONHEADERKEY) String sessionToken) {
        topicService.deleteTopic(new JSONObject(data).getInt("id"), sessionToken);
    }
}