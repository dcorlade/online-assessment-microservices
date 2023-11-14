package app.controllers;

import app.communication.StudentServiceCommunication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handler for http requests to the course service.
 */
@RestController
@RequestMapping("student_service")
public class CourseController {

    private final transient String headerSessionKey = "session";

    /**
     * Queries course service for a course entry.
     *
     * @param data         JSONObject (as String) with one key 'id' and as value
     *                     the courseId of the course to retrieve.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested course as a JSON object.
     */
    @PostMapping("courseById")
    @ResponseBody
    public ResponseEntity<String> courseById(@RequestBody String data,
                                             @RequestHeader(headerSessionKey) String sessionToken) {
        String response = StudentServiceCommunication.postRequest(data,
            "8082/courseService/course/courseById", sessionToken);
        if (response == null) {
            return new ResponseEntity("Course not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Enrolls the student in a course.
     *
     * @param data         Takes a JSON String representation of an enrollment to add.
     *                     This data has to be supplied in the body of the HTTP-request
     *                     in JSON format.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested enrollment as JSON object.
     */
    @PostMapping("enroll")
    @ResponseBody
    public ResponseEntity<String> enroll(@RequestBody String data,
                                         @RequestHeader(headerSessionKey) String sessionToken) {
        String response = StudentServiceCommunication.postRequest(data,
            "8082/courseService/enrollment/addEnrollment", sessionToken);
        if (response == null) {
            return new ResponseEntity("Course not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Unenrolls the student from a course.
     *
     * @param data         Takes a JSON String with one key 'id' and
     *                     as value the enrollmentId.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested enrollment as JSON object.
     */
    @PostMapping("unEnroll")
    @ResponseBody
    public ResponseEntity<String> unEnroll(@RequestBody String data,
                                           @RequestHeader(headerSessionKey) String sessionToken) {
        String response = StudentServiceCommunication.postRequest(data,
            "8082/courseService/enrollment/deleteEnrollment", sessionToken);
        if (response == null) {
            return new ResponseEntity("Enrollment not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Success", HttpStatus.OK);
    }

    /**
     * Queries the course service for whether the student
     * is enrolled in a particular course or not.
     *
     * @param data         Takes a JSON String with one key 'net_id' and
     *                     as value the UserId.
     * @param sessionToken A valid token received from the authorization service.
     * @return The requested enrollment as JSON object.
     */
    @PostMapping("getEnrollment")
    @ResponseBody
    public ResponseEntity<String> getEnrollment(@RequestBody String data,
                                                @RequestHeader(headerSessionKey)
                                                    String sessionToken) {
        String response = StudentServiceCommunication.postRequest(data,
            "8082/courseService/enrollment/getEnrollmentByUser", sessionToken);
        if (response == null) {
            return new ResponseEntity("Could not get enrollments for user", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
