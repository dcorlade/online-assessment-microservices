package app.services;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Question;
import app.repositories.QuestionRepository;
import app.serializerfactory.Serializer;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * This class will contain all logic for the endpoints found in QuestionController.
 * Every method will check whether or not the user is authorized and then perform some logic after
 * which it returns a ResponseEntity.
 */
@Service
public class QuestionService {

    private final transient QuestionRepository questionRepository;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();
    private final transient String errorMessage = "There exist no questions with that topicId";

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Given the questionId this method will return a ResponseEntity containing a
     * string representation of the Question object. If none is found it will return
     * 404 NOT_FOUND
     *
     * @param questionId   questionId
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> getQuestion(Integer questionId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        Question question = questionRepository.findById(questionId).orElseThrow();
        if (question == null) {
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(serializer.serialize(question), HttpStatus.OK);
    }

    /**
     * Given a topicId this method returns a ResponseEntity containing a string representation of
     * a JSONObject containing a JSONArray containing json
     * representations of all Questions with that topicId.
     *
     * @param topicId      topicId
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> getQuestionsByTopic(Integer topicId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        Object[] questions = questionRepository.findAll().stream()
            .filter(question -> question.getTopicId() == topicId)
            .toArray();
        if (questions.length == 0) {
            return new ResponseEntity<>(errorMessage,
                HttpStatus.NOT_FOUND);
        }
        JSONArray jsonArrayQuestions = new JSONArray();
        for (Object q : questions) {
            Question question = (Question) q;
            jsonArrayQuestions.put(serializer.serialize(question));
        }
        JSONObject jsonObjectQuestions = new JSONObject();
        jsonObjectQuestions.put("questions", jsonArrayQuestions);
        return new ResponseEntity<>(jsonObjectQuestions.toString(), HttpStatus.OK);
    }

    /**
     * Given a topicId this method returns a ResponseEntity containing a string representation of
     * a random Question with that given topicId.
     *
     * @param topicId      topicId
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> getRandomQuestionsByTopic(Integer topicId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        Object[] questions = questionRepository.findAll().stream()
            .filter(question -> question.getTopicId() == topicId)
            .toArray();
        if (questions.length == 0) {
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
        Question question = (Question) questions[new Random().nextInt(questions.length)];
        System.out.println(question.toString());
        return new ResponseEntity<>(serializer.serialize(question), HttpStatus.OK);
    }

    /**
     * Given a Question object it will attempt to store this in the database. If a question with the
     * same id already exists it will not store it.
     *
     * @param question     question
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> addQuestion(Question question, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        questionRepository.save(question);
        return new ResponseEntity<String>(serializer.serialize(question), HttpStatus.OK);
    }

    /**
     * Given a Question object it will attempt to update this in the database.
     * If no question with the
     * id exists it will return 404 NOT_FOUND.
     *
     * @param question     question
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> updateQuestion(Question question, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        if (questionRepository.findById(question.getId()).isEmpty()) {
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
        questionRepository.save(question);
        return new ResponseEntity<String>("Update was successful", HttpStatus.OK);
    }

    /**
     * Given a questionId this method will attempt to remove the related question from the database.
     * If no question with that id exists 404 NOT_FOUND will be returned.
     *
     * @param questionId   questionId
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> deleteQuestion(Integer questionId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        if (questionRepository.findById(questionId).isEmpty()) {
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
        questionRepository.deleteById(questionId);
        return new ResponseEntity<String>("Deletion was successful", HttpStatus.OK);
    }
}