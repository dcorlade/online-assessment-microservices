package app.controllers;

import app.communication.TeacherServiceCommunication;
import app.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handler for http requests to the course service concerning exam administration (CRUD).
 */
@RestController
@RequestMapping("teacher_service")
public class ExamController {

    /**
     * Queries the course service for an exam.
     *
     * @param data         JSONObject (as String) with "id" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested exam as a JSON object.
     */
    @PostMapping("examById")
    @ResponseBody
    public ResponseEntity<String> examById(@RequestBody String data,
                                           @RequestHeader(Constants.SESSIONHEADERKEY)
                                               String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8083/exam_service/examById", sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to update an exam.
     *
     * @param data         JSONObject (as String) with exam entity.
     * @param sessionToken A valid token received from the authorization service.
     * @return The updated exam as a JSON object.
     */
    @PostMapping("updateExam")
    @ResponseBody
    public ResponseEntity<String> updateExam(@RequestBody String data,
                                             @RequestHeader(Constants.SESSIONHEADERKEY)
                                                 String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8083/exam_service/updateExam", sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to update", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to create a new exam.
     *
     * @param data         JSONObject (as String) with exam entity.
     * @param sessionToken A valid token received from the authorization service.
     * @return The newly created exam as a JSON object.
     */
    @PostMapping("createExam")
    @ResponseBody
    public ResponseEntity<String> createExam(@RequestBody String data,
                                             @RequestHeader(Constants.SESSIONHEADERKEY)
                                                 String sessionToken) {
        String response =
            TeacherServiceCommunication
                // Since update and create are the same in exam service, this mapping is correct.
                .postRequest(data, "8083/exam_service/updateExam", sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to create", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to delete an exam.
     *
     * @param data         JSONObject (as String) with "id" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The response.
     */
    @PostMapping("deleteExam")
    @ResponseBody
    public ResponseEntity<String> deleteExam(@RequestBody String data,
                                             @RequestHeader(Constants.SESSIONHEADERKEY)
                                                 String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8083/exam_service/deleteExam", sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Success", HttpStatus.OK);
    }

    /**
     * Queries the exam service for a list of student exams.
     *
     * @param data         JSONObject (as String) with "examId" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested list of student exams as a JSONArray wrapped in a JSONObject.
     */
    @PostMapping("studentExamsByExamId")
    @ResponseBody
    public ResponseEntity<String> studentExamsByExamId(@RequestBody String data,
                                                       @RequestHeader(Constants.SESSIONHEADERKEY)
                                                           String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8083/exam_service/studentExamsByExamId", sessionToken);
        if (response == null) {
            return new ResponseEntity("Exam not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Get how many students took an exam.
     *
     * @param data         JSONObject (as String) with "examId" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested list of student exams as a JSONArray wrapped in a JSONObject.
     */
    @PostMapping("getAmountOfStudents")
    @ResponseBody
    public ResponseEntity<String> getAmountOfStudents(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8083/exam_service/getAmountOfStudents", sessionToken);
        if (response == null) {
            return new ResponseEntity("Exam not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Queries the course service for the average grade of an exam.
     *
     * @param data         JSONObject (as String) with "examId" key.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested grade.
     */
    @PostMapping("getAverageGrade")
    @ResponseBody
    public ResponseEntity<String> getAverageGrade(@RequestBody String data,
                                                  @RequestHeader(Constants.SESSIONHEADERKEY)
                                                      String sessionToken) {
        String response =
            TeacherServiceCommunication
                .postRequest(data, "8083/exam_service/getAverageGrade", sessionToken);
        if (response == null) {
            return new ResponseEntity("Entity not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
