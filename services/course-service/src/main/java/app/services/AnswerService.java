package app.services;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.repositories.AnswerRepository;
import app.serializerfactory.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/**
 * This class will contain all logic for the endpoints found in AnswerController.
 * Every method will check whether or not the user is authorized and then perform some logic after
 * which it returns a ResponseEntity.
 */
@Service
public class AnswerService {

    public final transient AnswerRepository answerRepository;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
     * Given the answerId this method will return a ResponseEntity
     * containing a string representation of the Answer object.
     *
     * @param answerId     id of the answer to be returned.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string representation of the answer
     */
    public ResponseEntity<String> getAnswer(Integer answerId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        try {
            Answer answer = answerRepository.findById(answerId).orElseThrow();
            return new ResponseEntity<String>(serializer.serialize(answer), HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exists no answer with that id";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Given a questionId this method returns a ResponseEntity containing a string representation of
     * a JSONObject containing a JSONArray containing json representations of all Answers with that
     * questionId.
     *
     * @param questionId   Integer of the questionId
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string representation of the answers in JSONArray format.
     */
    public ResponseEntity<String> getAnswersByQuestion(Integer questionId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        try {
            Object[] answers = answerRepository.findAll().stream()
                .filter(answer -> answer.getQuestionId() == questionId)
                .toArray();
            if (answers.length == 0) {
                throw new IllegalArgumentException();
            }
            JSONArray jsonArrayAnswers = new JSONArray();
            for (Object a : answers) {
                Answer answer = (Answer) a;
                jsonArrayAnswers.put(serializer.serialize(answer));
            }
            JSONObject jsonObjectAnswers = new JSONObject();
            jsonObjectAnswers.put("answers", jsonArrayAnswers);
            return new ResponseEntity<String>(jsonObjectAnswers.toString(), HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exist no answers with that questionId";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Given an Answer object it will attempt to store this in the database. If an answer with the
     * same id already exists it will not store it.
     *
     * @param answer       Answer object
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string representation of the answers in JSONArray format.
     */
    public ResponseEntity<String> addAnswer(Answer answer, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        answerRepository.save(answer);
        return new ResponseEntity<>(serializer.serialize(answer), HttpStatus.OK);
    }

    /**
     * Given an Answer object it will attempt to update this in the database. If no answer with the
     * id exists it will return 404 NOT_FOUND.
     *
     * @param answer       Answer object
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string representation of the answers in JSONArray format.
     */
    public ResponseEntity<String> updateAnswer(Answer answer, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }

        try {
            if (answerRepository.findById(answer.getId()).isEmpty()) {
                throw new IllegalArgumentException("Answer with this id doesn't exists");
            }
            answerRepository.save(answer);
            return new ResponseEntity<String>("Update was successful", HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exists no answer with that id";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Given an answerId this method will attempt to remove the related answer from the database.
     * If no answer with that id exists 404 NOT_FOUND will be returned.
     *
     * @param answerId     Integer UserId
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string
     *     representation of the answers in JSONArray format
     */
    public ResponseEntity<String> deleteAnswer(Integer answerId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        try {
            if (answerRepository.findById(answerId).isEmpty()) {
                throw new IllegalArgumentException();
            }
            answerRepository.deleteById(answerId);
            return new ResponseEntity<String>("Deletion was successful", HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exists no answer with that id";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
}