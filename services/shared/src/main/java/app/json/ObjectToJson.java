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
import org.json.JSONArray;
import org.json.JSONObject;

public class ObjectToJson {

    /**
     * Construct a Json String representation of the Answer object.
     *
     * @return a Json-representation of the object,
     *     containing all values that are not null.
     */
    public static String answer(Object object) {
        Answer answer = (Answer) object;
        JSONObject result = new JSONObject();
        if (answer.getId() != null) {
            result.put("id", answer.getId());
        }
        if (answer.getQuestionId() != null) {
            result.put("questionId", answer.getQuestionId());
        }
        if (answer.getOrder() != null) {
            result.put(Constants.ORDER_STRING, answer.getOrder());
        }
        if (answer.getDescription() != null) {
            result.put(Constants.DESCRIPTION_STRING, answer.getDescription());
        }
        if (answer.getCorrect() != null) {
            result.put(Constants.CORRECT_STRING, answer.getCorrect());
        }
        return result.toString();
    }

    /**
     * Construct a Json String representation of the Course object.
     *
     * @return a Json-representation of the object,
     *     containing all values that are not null.
     */
    public static String course(Object object) {
        Course course = (Course) object;
        JSONObject jsonObject = new JSONObject();
        if (course.getId() != null) {
            jsonObject.put("id", course.getId());
        }
        if (course.getCourseCode() != null) {
            jsonObject.put("courseCode", course.getCourseCode());
        }
        if (course.getName() != null) {
            jsonObject.put(Constants.NAME_STRING, course.getName());
        }
        if (course.getYear() != null) {
            jsonObject.put(Constants.YEAR_STRING, course.getYear());
        }
        if (course.getTopicList() != null) {
            JSONArray topicListJsonArray = new JSONArray();
            for (Topic topic : course.getTopicList()) {
                topicListJsonArray.put(ObjectToJson.topic(topic));
            }
            jsonObject.put("topicList", topicListJsonArray);
        }
        if (course.getEnrollmentList() != null) {
            JSONArray enrollmentListJsonArray = new JSONArray();
            for (Enrollment enrollment : course.getEnrollmentList()) {
                enrollmentListJsonArray.put(enrollment(enrollment));
            }
            jsonObject.put("enrollmentList", enrollmentListJsonArray);
        }
        return jsonObject.toString();
    }

    /**
     * Construct a Json String representation of the Enrollment object.
     *
     * @return a Json-representation of the object,
     *     containing all values (except the course attribute)
     *     that are not null.
     */
    public static String enrollment(Object object) {
        Enrollment enrollment = (Enrollment) object;
        JSONObject json = new JSONObject();
        if (enrollment.getId() != null) {
            json.put("id", enrollment.getId());
        }
        if (enrollment.getUserId() != null) {
            json.put("userId", enrollment.getUserId());
        }
        if (enrollment.getCourseId() != null) {
            json.put("courseId", enrollment.getCourseId());
        }
        return json.toString();
    }

    /**
     * Construct a Json String representation of the Exam object.
     *
     * @return a Json-representation of the object,
     *     containing all values that are not null.
     */
    public static String exam(Object object) {
        Exam exam = (Exam) object;
        JSONObject jsonObject = new JSONObject();
        if (exam.getId() != null) {
            jsonObject.put("id", exam.getId());
        }
        if (exam.getCourseId() != null) {
            jsonObject.put("courseId", exam.getCourseId());
        }
        if (exam.getStart() != null) {
            jsonObject.put(Constants.START_STRING, exam.getStart());
        }
        if (exam.getEnd() != null) {
            jsonObject.put(Constants.END_STRING, exam.getEnd());
        }
        if (exam.getStudentExamList() != null) {
            JSONArray studentExamListJsonArray = new JSONArray();
            for (StudentExam studentExam : exam.getStudentExamList()) {
                studentExamListJsonArray.put(studentExam(studentExam));
            }
            jsonObject.put("studentExamList", studentExamListJsonArray);
        }
        return jsonObject.toString();
    }

    /**
     * Construct a Json String representation of the ExamQuestion object.
     *
     * @return a Json-representation of the object,
     *     containing all values that are not null.
     */
    public static String examQuestion(Object object) {
        ExamQuestion examQuestion = (ExamQuestion) object;
        JSONObject json = new JSONObject();
        if (examQuestion.getId() != null) {
            json.put("id", examQuestion.getId());
        }
        if (examQuestion.getStudentExamId() != null) {
            json.put("studentExamId", examQuestion.getStudentExamId());
        }
        if (examQuestion.getQuestion() != null) {
            json.put(Constants.QUESTION_STRING_FACTORY, examQuestion.getQuestion());
        }
        if (examQuestion.getCorrect() != null) {
            json.put(Constants.CORRECT_STRING, examQuestion.getCorrect());
        }
        if (examQuestion.getStudentAnswers() != null) {
            JSONArray a = new JSONArray();
            for (StudentAnswer s : examQuestion.getStudentAnswers()) {
                a.put(studentAnswer(s));
            }
            json.put("studentAnswers", a);
        }
        return json.toString();
    }

    /**
     * Construct a Json representation of the Question object.
     *
     * @return Json String representation of the Question object.
     */
    public static String question(Object object) {
        Question question = (Question) object;
        JSONObject json = new JSONObject();
        if (question.getId() != null) {
            json.put("id", question.getId());
        }
        if (question.getTopicId() != null) {
            json.put("topicId", question.getTopicId());
        }
        if (question.getTitle() != null) {
            json.put(Constants.TITLE_STRING, question.getTitle());
        }
        if (question.getDescription() != null) {
            json.put(Constants.DESCRIPTION_STRING, question.getDescription());
        }
        if (question.getAnswers() != null) {
            JSONArray a = new JSONArray();
            for (Answer x : question.getAnswers()) {
                a.put(answer(x));
            }
            json.put("answers", a);
        }
        return json.toString();
    }

    /**
     * Construct a Json representation of the StudentAnswer object.
     *
     * @return Json String representation of the StudentAnswer object.
     */
    public static String studentAnswer(Object object) {
        StudentAnswer studentAnswer = (StudentAnswer) object;
        JSONObject json = new JSONObject();
        if (studentAnswer.getId() != null) {
            json.put("id", studentAnswer.getId());
        }
        if (studentAnswer.getExamQuestionId() != null) {
            json.put("examQuestionId", studentAnswer.getExamQuestionId());
        }
        if (studentAnswer.getAnswer() != null) {
            json.put(Constants.ANSWER_STRING, studentAnswer.getAnswer());
        }
        if (studentAnswer.getSelected() != null) {
            json.put(Constants.SELECTED_STRING, studentAnswer.getSelected());
        }
        return json.toString();
    }

    /**
     * Construct a Json representation of the StudentExam object.
     *
     * @return Json String representation of the StudentExam object.
     */
    public static String studentExam(Object object) {
        StudentExam studentExam = (StudentExam) object;
        JSONObject json = new JSONObject();
        if (studentExam.getId() != null) {
            json.put("id", studentExam.getId());
        }
        if (studentExam.getCorrectQuestions() != null) {
            json.put("correctQuestions", studentExam.getCorrectQuestions());
        }
        if (studentExam.getGrade() != null) {
            json.put(Constants.GRADE_STRING, studentExam.getGrade());
        }
        if (studentExam.getExtraTime() != null) {
            json.put("extraTime", studentExam.getExtraTime());
        }
        if (studentExam.getUser() != null) {
            json.put("userId", studentExam.getUser());
        }
        if (studentExam.getExamId() != null) {
            json.put("examId", studentExam.getExamId());
        }
        if (studentExam.getStartingTime() != null) {
            json.put("startingTime", studentExam.getStartingTime());
        }
        if (studentExam.getExamQuestions() != null) {
            JSONArray a = new JSONArray();
            for (ExamQuestion q : studentExam.getExamQuestions()) {
                a.put(examQuestion(q));
            }
            json.put("examQuestions", a);
        }
        return json.toString();
    }

    /**
     * Construct a Json representation of the Topic object.
     *
     * @return Json String representation of the Topic object.
     */
    public static String topic(Object object) {
        Topic topic = (Topic) object;
        JSONObject json = new JSONObject();
        if (topic.getId() != null) {
            json.put("id", topic.getId());
        }
        if (topic.getName() != null) {
            json.put(Constants.NAME_STRING, topic.getName());
        }
        if (topic.getCourseId() != null) {
            json.put("courseId", topic.getCourseId());
        }
        if (topic.getQuestions() != null) {
            JSONArray a = new JSONArray();
            for (Question q : topic.getQuestions()) {
                a.put(question(q));
            }
            json.put("questions", a);
        }
        return json.toString();
    }

    /**
     * Construct a Json representation of the User object.
     *
     * @return Json String representation of the User object.
     */
    public static String user(Object object) {
        User user = (User) object;
        JSONObject json = new JSONObject();
        if (user.getNetId() != null) {
            json.put("netId", user.getNetId());
        }
        if (user.getRole() != null) {
            json.put(Constants.ROLE_STRING, user.getRole());
        }
        if (user.getPassword() != null) {
            json.put(Constants.PASSWORD_STRING, user.getPassword());
        }
        if (user.getExtraTime() != null) {
            json.put("extraTime", user.getExtraTime());
        }
        return json.toString();
    }
}
