package app.controllers;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Answer;
import app.models.ExamQuestion;
import app.models.Question;
import app.models.StudentAnswer;
import app.models.StudentExam;
import app.models.Topic;
import app.repositories.QuestionRepository;
import app.repositories.TopicRepository;
import app.serializerfactory.Serializer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("course_service")
public class ExamApiController {
    private final transient TopicRepository topicRepository;
    private final transient QuestionRepository questionRepository;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    public ExamApiController(TopicRepository topicRepository,
                             QuestionRepository questionRepository) {
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Retrieve List of ExamQuestion by course id.
     *
     * @param data         JSON String with "courseId" key and as a value an CourseId.
     * @param sessionToken session token.
     * @return JSONObject with "examQuestionList" key and JSONArray of ExamQuestions as value.
     */
    @PostMapping("examQuestionsByCourseId")
    public ResponseEntity<String> examQuestionsByCourseId(@RequestBody String data,
                                                          @RequestHeader(Constants.SESSIONHEADERKEY)
                                                              String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        Integer courseId = getCourseIdFromData(data);
        List<Topic> topics = topicRepository.findByCourseId(courseId);
        int upperLimitTopics = 10;
        if (topics.size() > upperLimitTopics) {
            return new ResponseEntity<String>(
                "There are more than 10 topics related to this course", HttpStatus.CONFLICT);
        }

        List<Question> questions = new ArrayList<>(10);

        // Next two lines fix a stupid PMD error
        questions.add(new Question());
        questions.remove(0);

        if (!addOneQuestionFromEachTopic(questions, topics)) {
            return new ResponseEntity<String>(
                "There are topics without questions", HttpStatus.CONFLICT);
        }
        if (!fillQuestionsToTen(questions, topics)) {
            return new ResponseEntity<String>(
                "There are not enough questions", HttpStatus.NOT_FOUND);
        }

        return questionsToResponseEntity(questions);
    }

    private Integer getCourseIdFromData(String data) {
        return new JSONObject(data).getInt("courseId");
    }

    private ResponseEntity<String> questionsToResponseEntity(List<Question> questions) {
        JSONObject response = new JSONObject();
        response.put("studentExam", serializer.serialize(questionsToStudentExam(questions)));
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    /**
     * Retrieve List of Question by id.
     *
     * @param data         JSON String with "id" key and as a value a QuestionId.
     * @param sessionToken session token.
     * @return JSONObject with "questionList" key and JSONArray of questions as value.
     */
    @PostMapping("getQuestionsById")
    public ResponseEntity<String> getQuestionsById(@RequestBody String data,
                                                   @RequestHeader(Constants.SESSIONHEADERKEY)
                                                       String sessionToken) {
        JSONObject json = new JSONObject(data);
        JSONArray jsonIds = new JSONArray((String) json.get("questionIds"));
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < jsonIds.length(); i++) {
            ids.add((int) jsonIds.get(i));
        }
        List<Question> questions = questionRepository.findAllById(ids);
        JSONObject response = new JSONObject();
        JSONArray a = new JSONArray();
        for (Question q : questions) {
            a.put(serializer.serialize(q));
        }
        response.put("questions", a.toString());
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    /**
     * Create a StudentExam object with a list of questions.
     *
     * @param questions List of questions
     * @return JSONObject with "examQuestionList" key and JSONArray of ExamQuestions as value.
     */
    public StudentExam questionsToStudentExam(List<Question> questions) {
        StudentExam exam = new StudentExam();
        List<ExamQuestion> examQuestions = new ArrayList<>();
        for (Question q : questions) {
            ExamQuestion eq = new ExamQuestion();
            eq.setQuestion(q.getId());
            List<StudentAnswer> studentAnswers = new ArrayList<>();
            for (Answer a : q.getAnswers()) {
                StudentAnswer sa = new StudentAnswer();
                sa.setAnswer(a.getId());
                sa.setSelected(false);
                studentAnswers.add(sa);
            }
            eq.setStudentAnswers(studentAnswers);
            examQuestions.add(eq);
        }
        examQuestions.sort(Comparator.comparing(ExamQuestion::getQuestion));
        exam.setExamQuestions(examQuestions);
        return exam;
    }

    private boolean addOneQuestionFromEachTopic(List<Question> questions, List<Topic> topics) {
        for (Topic t : topics) {
            List<Question> temp = t.getQuestions();
            if (temp.isEmpty()) {
                return false;
            }
            questions.add(temp.remove((int) new Random().nextInt(temp.size())));
        }
        return true;
    }

    private boolean fillQuestionsToTen(List<Question> questions, List<Topic> topics) {
        List<Question> poll = topics.stream()
            .flatMap(x -> x.getQuestions().stream())
            .collect(Collectors.toList());
        Collections.shuffle(poll);
        while (questions.size() < 10) {
            if (poll.isEmpty()) {
                return false;
            }
            questions.add(poll.remove(0));
        }
        return true;
    }
}