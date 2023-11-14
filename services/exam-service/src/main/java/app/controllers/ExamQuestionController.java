package app.controllers;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.ExamQuestion;
import app.repositories.ExamQuestionRepository;
import app.serializerfactory.Serializer;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("exam_service")
public class ExamQuestionController {

    private final transient ExamQuestionRepository examQuestionRepository;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    /**
     * Autowired constructor.
     *
     * @param examQuestionRepository ExamQuestionRepository.
     */
    @Autowired
    public ExamQuestionController(ExamQuestionRepository examQuestionRepository) {
        this.examQuestionRepository = examQuestionRepository;

    }

    /**
     * Get examQuestion by id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return JSONObject with ExamQuestion.
     */
    @PostMapping("examQuestionById")
    public ResponseEntity<String> examQuestionById(@RequestBody String data,
                                                   @RequestHeader(Constants.SESSIONHEADERKEY)
                                                       String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        ExamQuestion examQuestion =
            examQuestionRepository.findById((int) json.get("id"));
        if (examQuestion == null) {
            return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(serializer.serialize(examQuestion), HttpStatus.OK);
    }

    /**
     * Update ExamQuestion in database.
     *
     * @param data         JSONObject with ExamQuestion.
     * @param sessionToken session token.
     * @return JSONObject with ExamQuestion.
     */
    @PostMapping("updateExamQuestion")
    public ResponseEntity<String> updateExamQuestion(@RequestBody String data,
                                                     @RequestHeader(Constants.SESSIONHEADERKEY)
                                                         String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        ExamQuestion examQuestion =
            (ExamQuestion) serializer.deserialize(json.toString(), ExamQuestion.class);
        examQuestion = examQuestionRepository.save(examQuestion);
        return new ResponseEntity<>(serializer.serialize(examQuestion), HttpStatus.OK);
    }

    /**
     * Delete ExamQuestion by id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return "Success" if deletion successful.
     */
    @PostMapping("deleteExamQuestion")
    public ResponseEntity<String> deleteExamQuestion(@RequestBody String data,
                                                     @RequestHeader(Constants.SESSIONHEADERKEY)
                                                         String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        int id = (int) json.get("id");
        examQuestionRepository.deleteById(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * Retrive List of ExamQuestion by exam id.
     *
     * @param data         JSONObject with "id" key.
     * @param sessionToken session token.
     * @return JSONObject with "examQuestionList" key and JSONArray of ExamQuestions as value.
     */
    @PostMapping("examQuestionsByStudentExamId")
    public ResponseEntity<String> examQuestionsByStudentExamId(@RequestBody String data,
                                                @RequestHeader(Constants.SESSIONHEADERKEY)
                                                                   String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            throw new SecurityException();
        }
        JSONObject json = new JSONObject(data);
        List<ExamQuestion> examQuestionList = examQuestionRepository
            .findExamQuestionsByStudentExamId((int) json.get("id"));
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (ExamQuestion e : examQuestionList) {
            jsonArray.put(serializer.serialize(e));
        }
        jsonObject.put("examQuestionList", jsonArray);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}

