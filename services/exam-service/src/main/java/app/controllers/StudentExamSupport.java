package app.controllers;

import app.communication.ExamServiceCommunication;
import app.json.JsonSerializerFactory;
import app.models.Exam;
import app.models.ExamQuestion;
import app.models.Question;
import app.models.StudentExam;
import app.repositories.StudentExamRepository;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class StudentExamSupport {

    private final transient StudentExamRepository studentExamRepository;
    private final transient int three = 3;
    private final transient String userIdStr = "userId";
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();


    public StudentExamSupport(StudentExamRepository studentExamRepository) {
        this.studentExamRepository = studentExamRepository;
    }

    /**
     * Checks whether exam limit was reached.
     *
     * @param exId   exam id.
     * @param userId user id.
     * @return boolean.
     */
    public boolean isExamLimitReached(int exId, String userId) {
        List<StudentExam> studentExams =
            studentExamRepository.findByExamAndUser(exId, userId);
        return (studentExams.size() >= three);
    }

    /**
     * Checks whether current time is not before and not after exam time.
     *
     * @param exam Exam entity.
     * @return boolean.
     */
    public boolean isExamTimeCorrect(Exam exam) {
        Timestamp start = exam.getStart();
        Timestamp end = exam.getEnd();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        return current.after(start) && current.before(end);
    }

    /**
     * Ask course service to generate StudentExam.
     *
     * @param courseId     course id.
     * @param sessionToken session token.
     * @return StudentExam entity.
     */
    public StudentExam studentExamFromCourseService(int courseId, String sessionToken) {
        JSONObject courseIdJson = new JSONObject();
        courseIdJson.put("courseId", courseId);
        String response =
            ExamServiceCommunication
                .postRequest(courseIdJson.toString(), "8082/course_service/examQuestionsByCourseId",
                    sessionToken);
        if (response == null) {
            return null;
        }
        return (StudentExam) serializer.deserialize(response, StudentExam.class);
    }

    /**
     * Ask authentication_service for extra time.
     *
     * @param userId       user id.
     * @param sessionToken session token.
     * @return number of minutes that certain user have as extra time.
     */
    public Integer studentExamExtraTime(String userId, String sessionToken) {
        JSONObject jsonUserId = new JSONObject();
        jsonUserId.put(userIdStr, userId);
        String response =
            ExamServiceCommunication
                .postRequest(jsonUserId.toString(), "8080/authentication_service/getExtraTime",
                    sessionToken);
        if (response == null) {
            return null;
        }
        return (int) new JSONObject(response).get("extraTime");
    }

    /**
     * Checks whether exam has ended.
     *
     * @param startingTime starting time of exam.
     * @param extraTime    extra time in minutes.
     * @return boolean.
     */
    public boolean hasExamEnded(Timestamp startingTime, int extraTime) {
        long time = ((long) extraTime * 60) * 1000 + (20 * 60) * 1000;
        Timestamp end = new Timestamp(startingTime.getTime() + time);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        return current.after(end);
    }

    /**
     * Ask List of Questions from course service that correspond to ExamQuestions.
     *
     * @param studentQuestions list of ExamQuestions.
     * @param sessionToken     session token.
     * @return List of Questions.
     */
    public List<Question> getCorrectQuestionsFromCourse(List<ExamQuestion> studentQuestions,
                                                        String sessionToken) {
        JSONObject jsonQuestionIds = new JSONObject();
        JSONArray questionIds = new JSONArray();
        for (ExamQuestion examQuestion : studentQuestions) {
            questionIds.put(examQuestion.getQuestion());
        }
        jsonQuestionIds.put("questionIds", questionIds);
        String response =
            ExamServiceCommunication
                .postRequest(jsonQuestionIds.toString(), "8082/course_service/getQuestionsById",
                    sessionToken);
        if (response == null) {
            return null;
        }
        List<Question> correctQuestions = new ArrayList<>();
        JSONArray questions = (JSONArray) new JSONObject(response).get("questions");
        for (int i = 0; i < questions.length(); i++) {
            Question q = (Question) serializer
                .deserialize(new JSONObject(questions.get(i).toString()).toString(),
                    Question.class);
            correctQuestions.add(q);
        }
        return correctQuestions;
    }

    /**
     * Given questions answered student and original questions, count number of correct answers.
     *
     * @param studentQuestions List of ExamQuestions.
     * @param correctQuestions List of Questions.
     * @return number of correctly answered questions.
     */
    public Integer countCorrectQuestions(List<ExamQuestion> studentQuestions,
                                         List<Question> correctQuestions) {
        int correctAnswers = 0;
        for (int i = 0; i < studentQuestions.size(); i++) {
            boolean correct = true;
            for (int j = 0; j < studentQuestions.get(i).getStudentAnswers().size(); j++) {
                if (studentQuestions.get(i).getStudentAnswers().get(j).getSelected()
                    != correctQuestions.get(i).getAnswers().get(j).getCorrect()) {
                    correct &= false;
                }
            }
            if (correct) {
                correctAnswers += 1;
            }
            studentQuestions.get(i).setCorrect(correct);
        }
        return correctAnswers;
    }
}
