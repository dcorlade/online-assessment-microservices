package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Question;
import app.serializerfactory.Serializer;
import app.services.QuestionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courseService/question")
public class QuestionController {
    private final transient QuestionService questionService;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    /**
     * Constructs a QuestionController.
     *
     * @param questionService Takes an questionService to assign to its instance variable.
     *                        The constructor
     *                        is Autowired, meaning that Spring will
     *                        take the instance that is has and
     *                        assign it to the variable automatically.
     */
    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of retrieving a question from the database.
     *
     * @param data         JSON String with "id" as key and a QuestionId as a value.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the question in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getQuestion")
    public ResponseEntity<String> getQuestion(@RequestBody String data,
                                              @RequestHeader(Constants.SESSIONHEADERKEY)
                                                  String sessionToken) {
        return questionService.getQuestion(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of retrieving a question from the database.
     *
     * @param data         JSON String with "id" as key and a topicId as a value.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the
     *     question related to the provided topicId in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getQuestionByTopicId")
    public ResponseEntity<String> getQuestionsByTopic(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        return questionService
            .getQuestionsByTopic(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of adding a question to the database.
     *
     * @param data         Takes a String representation of an Question to add.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/createQuestion")
    public ResponseEntity<String> createQuestion(@RequestBody String data,
                                                 @RequestHeader(Constants.SESSIONHEADERKEY)
                                                     String sessionToken) {
        return questionService
            .addQuestion((Question) serializer.deserialize(data, Question.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of updating a question in the database.
     *
     * @param data         Takes a String representation of an Question to overwrite.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/updateQuestion")
    public ResponseEntity<String> updateQuestion(@RequestBody String data,
                                                 @RequestHeader(Constants.SESSIONHEADERKEY)
                                                     String sessionToken) {
        return questionService
            .updateQuestion((Question) serializer.deserialize(data, Question.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices to
     * the logic of deleting a question from the database.
     *
     * @param data         JSON String with "id" as key and a questionId as a value.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping(path = "/deleteQuestion")
    public ResponseEntity<String> deleteQuestion(@RequestBody String data,
                                                 @RequestHeader(Constants.SESSIONHEADERKEY)
                                                     String sessionToken) {
        return questionService.deleteQuestion(new JSONObject(data).getInt("id"), sessionToken);
    }
}