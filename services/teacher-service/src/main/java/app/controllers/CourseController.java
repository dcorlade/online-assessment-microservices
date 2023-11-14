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
 * Handler for http requests to the course service concerning course administration (CRUD).
 */
@RestController
@RequestMapping("teacher_service")
public class CourseController {

    /**
     * Requests the course service to create a new course.
     *
     * @param data         A JSONObject (as String) with the course entity.
     * @param sessionToken A valid token received from the authorization service.
     * @return The newly created course as a JSON object.
     */
    @PostMapping("createCourse")
    @ResponseBody
    public ResponseEntity<String> createCourse(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/course/createCourse", sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to create course", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to update a particular course.
     *
     * @param data         A JSONObject (as String) with the course entity.
     * @param sessionToken A valid token received from the authorization service.
     * @return The updated course as a JSON object.
     */
    @PostMapping("updateCourse")
    @ResponseBody
    public ResponseEntity<String> updateCourse(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/course/updateCourse", sessionToken);
        if (response == null) {
            return new ResponseEntity("Failed to update course", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Requests the course service to delete a particular course.
     *
     * @param data         A JSONObject (as String) with the course entity.
     * @param sessionToken A valid token received from the authorization service.
     * @return The response.
     */
    @PostMapping("deleteCourse")
    @ResponseBody
    public ResponseEntity<String> deleteCourse(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/course/deleteCourse", sessionToken);
        if (response == null) {
            return new ResponseEntity("Course not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Success!", HttpStatus.OK);
    }

    /**
     * Queries the course service for a course entry.
     *
     * @param data         Takes a JSON with one key 'id' and
     *                     as value the courseId of the course to retrieve.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested course as a JSON object.
     */
    @PostMapping("courseById")
    @ResponseBody
    public ResponseEntity<String> courseById(@RequestBody String data,
                                             @RequestHeader(Constants.SESSIONHEADERKEY)
                                                 String sessionToken) {
        String response = TeacherServiceCommunication.postRequest(data,
            "8082/courseService/course/courseById", sessionToken);
        if (response == null) {
            return new ResponseEntity("Course not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
