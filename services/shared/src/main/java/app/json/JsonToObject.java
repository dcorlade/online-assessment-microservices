package app.json;

import app.constants.Constants;
import app.models.Answer;
import app.models.Course;
import app.models.Enrollment;
import app.models.Exam;
import app.models.ExamQuestion;
import app.models.Question;
import app.models.StudentAnswer;
import app.models.StudentExam;
import app.models.Topic;
import app.models.User;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonToObject {

    /**
     * Fills an Answer object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object answer(String string) {
        JSONObject json = new JSONObject(string);
        Answer answer = new Answer();
        if (json.has("id")) {
            answer.setId(json.getInt("id"));
        }
        if (json.has("questionId")) {
            answer.setQuestionId(json.getInt("questionId"));
        }
        if (json.has(Constants.ORDER_STRING)) {
            answer.setOrder(json.getInt(Constants.ORDER_STRING));
        }
        if (json.has(Constants.DESCRIPTION_STRING)) {
            answer.setDescription(json.getString(Constants.DESCRIPTION_STRING));
        }
        if (json.has(Constants.CORRECT_STRING)) {
            answer.setCorrect(json.getBoolean(Constants.CORRECT_STRING));
        }
        return answer;
    }

    /**
     * Fills a Course object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object course(String string) {
        JSONObject json = new JSONObject(string);
        Course course = new Course();
        if (json.has("id")) {
            course.setId((int) json.get("id"));
        }
        if (json.has("courseCode")) {
            course.setCourseCode((String) json.get("courseCode"));
        }
        if (json.has(Constants.NAME_STRING)) {
            course.setName((String) json.get(Constants.NAME_STRING));
        }
        if (json.has(Constants.YEAR_STRING)) {
            course.setYear((String) json.get(Constants.YEAR_STRING));
        }
        if (json.has("topicList")) {
            JSONArray topicListJson = (JSONArray) json.get("topicList");
            List<Topic> topicList = new ArrayList<>();
            for (int i = 0; i < topicListJson.length(); i++) {
                topicList.add(
                    (Topic) JsonToObject
                        .topic(new JSONObject(topicListJson.get(i).toString()).toString()));
            }
            course.setTopicList(topicList);
        }
        if (json.has("enrollmentList")) {
            JSONArray enrollmentListJson = (JSONArray) json.get("enrollmentList");
            List<Enrollment> enrollmentList = new ArrayList<>();
            for (int i = 0; i < enrollmentListJson.length(); i++) {
                enrollmentList
                    .add((Enrollment) JsonToObject.enrollment(
                        new JSONObject(enrollmentListJson.get(i).toString()).toString()));
            }
            course.setEnrollmentList(enrollmentList);
        }
        return course;
    }

    /**
     * Fills an Enrollment object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object enrollment(String string) {
        JSONObject json = new JSONObject(string);
        Enrollment enrollment = new Enrollment();
        if (json.has("id")) {
            enrollment.setId((int) json.get("id"));
        }
        if (json.has(Constants.USER_ID_STRING)) {
            enrollment.setUserId((String) json.get(Constants.USER_ID_STRING));
        }
        if (json.has(Constants.COURSE_ID)) {
            enrollment.setCourseId((int) json.get(Constants.COURSE_ID));
        }
        return enrollment;
    }

    /**
     * Fills an Exam object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object exam(String string) {
        JSONObject json = new JSONObject(string);
        Exam exam = new Exam();
        if (json.has("id")) {
            exam.setId((Integer) json.get("id"));
        }
        if (json.has(Constants.COURSE_ID)) {
            exam.setCourseId((Integer) json.get(Constants.COURSE_ID));
        }
        if (json.has(Constants.START_STRING)) {
            exam.setStart(Timestamp.valueOf((String) json.get(Constants.START_STRING)));
        }
        if (json.has(Constants.END_STRING)) {
            exam.setEnd(Timestamp.valueOf((String) json.get(Constants.END_STRING)));
        }
        if (json.has("studentExamList")) {
            JSONArray studentExamListJson = (JSONArray) json.get("studentExamList");
            List<StudentExam> studentExamList = new ArrayList<>();
            for (int i = 0; i < studentExamListJson.length(); i++) {

                studentExamList
                    .add((StudentExam) studentExam(
                        new JSONObject(studentExamListJson.get(i).toString()).toString()));
            }
            exam.setStudentExamList(studentExamList);
        }
        return exam;
    }

    /**
     * Fills an ExamQuestion object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object examQuestion(String string) {
        JSONObject json = new JSONObject(string);
        ExamQuestion examQuestion = new ExamQuestion();
        if (json.has("id")) {
            examQuestion.setId((int) json.get("id"));
        }
        if (json.has("studentExamId")) {
            examQuestion.setStudentExamId((int) json.get("studentExamId"));
        }
        if (json.has(Constants.CORRECT_STRING)) {
            examQuestion.setCorrect((boolean) json.get(Constants.CORRECT_STRING));
        }
        if (json.has(Constants.QUESTION_STRING_FACTORY)) {
            examQuestion.setQuestion((int) json.get(Constants.QUESTION_STRING_FACTORY));
        }
        if (json.has("studentAnswers")) {
            JSONArray a = (JSONArray) json.get("studentAnswers");
            List<StudentAnswer> studentAnswers = new ArrayList<>();
            for (int i = 0; i < a.length(); i++) {

                studentAnswers.add(
                    (StudentAnswer) studentAnswer(new JSONObject(a.get(i).toString()).toString()));
            }
            examQuestion.setStudentAnswers(studentAnswers);
        }
        return examQuestion;
    }

    /**
     * Fills an Question object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object question(String string) {
        JSONObject json = new JSONObject(string);
        Question question = new Question();
        if (json.has("id")) {
            question.setId((int) json.get("id"));
        }
        if (json.has(Constants.TITLE_STRING)) {
            question.setTitle((String) json.get(Constants.TITLE_STRING));
        }
        if (json.has("topicId")) {
            question.setTopicId((int) json.get("topicId"));
        }
        if (json.has(Constants.DESCRIPTION_STRING)) {
            question.setDescription((String) json.get(Constants.DESCRIPTION_STRING));
        }
        if (json.has("answers")) {
            JSONArray a = (JSONArray) json.get("answers");
            List<Answer> answers = new ArrayList<>();
            for (int i = 0; i < a.length(); i++) {

                answers.add((Answer) answer(new JSONObject(a.get(i).toString()).toString()));
            }
            question.setAnswers(answers);
        }
        return question;
    }

    /**
     * Fills a StudentAnswer object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object studentAnswer(String string) {
        JSONObject json = new JSONObject(string);
        StudentAnswer studentAnswer = new StudentAnswer();
        if (json.has("id")) {
            studentAnswer.setId((int) json.get("id"));
        }
        if (json.has("examQuestionId")) {
            studentAnswer.setExamQuestionId((int) json.get("examQuestionId"));
        }
        if (json.has(Constants.ANSWER_STRING)) {
            studentAnswer.setAnswer((int) json.get(Constants.ANSWER_STRING));
        }
        if (json.has(Constants.SELECTED_STRING)) {
            studentAnswer.setSelected((boolean) json.get(Constants.SELECTED_STRING));
        }
        return studentAnswer;
    }

    /**
     * Fills a StudentExam object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object studentExam(String string) {
        JSONObject json = new JSONObject(string);
        StudentExam studentExam = new StudentExam();
        if (json.has("id")) {
            studentExam.setId((Integer) json.get("id"));
        }
        if (json.has("correctQuestions")) {
            studentExam.setCorrectQuestions((Integer) json.get("correctQuestions"));
        }
        if (json.has(Constants.GRADE_STRING)) {
            Float grade = json.getFloat(Constants.GRADE_STRING);
            studentExam.setGrade(grade);
        }
        if (json.has(Constants.EXTRA_TIME)) {
            studentExam.setExtraTime((Integer) json.get(Constants.EXTRA_TIME));
        }
        if (json.has(Constants.USER_ID_STRING)) {
            studentExam.setUser((String) json.get(Constants.USER_ID_STRING));
        }
        if (json.has("examId")) {
            studentExam.setExamId((Integer) json.get("examId"));
        }
        if (json.has("startingTime")) {
            studentExam.setStartingTime(Timestamp.valueOf((String) json.get("startingTime")));
        }
        if (json.has("examQuestions")) {
            JSONArray a = (JSONArray) json.get("examQuestions");
            List<ExamQuestion> examQuestions = new ArrayList<>();
            for (int i = 0; i < a.length(); i++) {

                examQuestions.add(
                    (ExamQuestion) examQuestion(new JSONObject(a.get(i).toString()).toString()));
            }
            studentExam.setExamQuestions(examQuestions);
        }
        return studentExam;
    }

    /**
     * Fills a Topic object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object topic(String string) {
        JSONObject json = new JSONObject(string);
        Topic topic = new Topic();
        if (json.has("id")) {
            topic.setId((int) json.get("id"));
        }
        if (json.has(Constants.NAME_STRING)) {
            topic.setName((String) json.get(Constants.NAME_STRING));
        }
        if (json.has(Constants.COURSE_ID)) {
            topic.setCourseId((int) json.get(Constants.COURSE_ID));
        }
        if (json.has("questions")) {
            JSONArray a = (JSONArray) json.get("questions");
            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < a.length(); i++) {
                questions.add((Question) question(new JSONObject(a.get(i).toString()).toString()));
            }
            topic.setQuestions(questions);
        }
        return topic;
    }

    /**
     * Fills an User object using a Json-String.
     *
     * @param string A Json-String containing all Answer-attributes with its values.
     */
    public static Object user(String string) {
        JSONObject json = new JSONObject(string);
        User user = new User();
        if (json.has("netId")) {
            user.setNetId((String) json.get("netId"));
        }
        if (json.has(Constants.PASSWORD_STRING)) {
            user.setPassword((String) json.get(Constants.PASSWORD_STRING));
        }
        if (json.has(Constants.ROLE_STRING)) {
            user.setRole((Integer) json.get(Constants.ROLE_STRING));
        }
        if (json.has(Constants.EXTRA_TIME)) {
            user.setExtraTime((Integer) json.get(Constants.EXTRA_TIME));
        }
        return user;
    }

}
