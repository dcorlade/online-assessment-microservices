package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.serializerfactory.Serializer;
import app.services.AnswerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courseService/answer")
public class AnswerController {
    private final transient AnswerService answerService;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    /**
     * Constructs a AnswerController.
     *
     * @param answerService Takes an answerService to assign to its instance variable.
     *                      The constructor is Autowired, meaning that Spring will
     *                      take the instance that is has and
     *                      assign it to the variable automatically.
     */
    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of retrieving an answer from the database.
     *
     * @param data         JSON-String with "id" key and a AnswerId as a value.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the answer in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getAnswer")
    public ResponseEntity<String> getAnswer(@RequestBody String data,
                                            @RequestHeader(Constants.SESSIONHEADERKEY)
                                                String sessionToken) {
        return answerService.getAnswer(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of retrieving a answer from the database.
     *
     * @param data         JSON-String with "id" key and a QuestionId as a value.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the
     *     answers related to the provided questionId in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getAnswerByQuestionId")
    public ResponseEntity<String> getAnswersByQuestionId(@RequestBody String data,
                                                         @RequestHeader(Constants.SESSIONHEADERKEY)
                                                             String sessionToken) {
        return answerService
            .getAnswersByQuestion(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of adding a answer to the database.
     *
     * @param data         Takes a String representation of an Answer to add.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/createAnswer")
    public ResponseEntity<String> createAnswer(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        return answerService
            .addAnswer((Answer) serializer.deserialize(data, Answer.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of updating a answer in the database.
     *
     * @param data         Takes a String representation of an Answer to overwrite.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/updateAnswer")
    public ResponseEntity<String> updateAnswer(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        return answerService
            .updateAnswer((Answer) serializer.deserialize(data, Answer.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of deleting a answer from the database.
     *
     * @param data         JSON-String with a key 'id' and as value an AnswerId.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     */
    @PostMapping("/deleteAnswer")
    public void deleteAnswer(@RequestBody String data,
                             @RequestHeader(Constants.SESSIONHEADERKEY) String sessionToken) {
        answerService.deleteAnswer(new JSONObject(data).getInt("id"), sessionToken);
    }
}