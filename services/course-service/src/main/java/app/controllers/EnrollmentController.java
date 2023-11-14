package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Enrollment;
import app.serializerfactory.Serializer;
import app.services.EnrollmentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courseService/enrollment")
public class EnrollmentController {
    private final transient EnrollmentService enrollmentService;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    /**
     * Constructs a EnrollmentController.
     *
     * @param enrollmentService Takes an enrollmentService to assign to its instance variable.
     *                          The constructor
     *                          is Autowired, meaning that Spring will take
     *                          the instance that is has and
     *                          assign it to the variable automatically.
     */
    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of retrieving an enrollment from the database.
     *
     * @param data         Takes an JSON String with one key 'id' and
     *                     as value the enrollmentId of the
     *                     enrollment to retrieve.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the enrollment in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getEnrollment")
    public ResponseEntity<String> getEnrollment(@RequestBody String data,
                                                @RequestHeader(Constants.SESSIONHEADERKEY)
                                                    String sessionToken) {
        return enrollmentService.getEnrollment(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices to the
     * logic of retrieving an enrollment from the database.
     *
     * @param data         Takes an JSON String with one key 'id' and as value the courseId.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the
     *     enrollments related to the provided courseId in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getEnrollmentByCourse")
    public ResponseEntity<String> getEnrollmentByCourse(@RequestBody String data,
                                                        @RequestHeader(Constants.SESSIONHEADERKEY)
                                                            String sessionToken) {
        return enrollmentService
            .getEnrollmentByCourse(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices to
     * the logic of retrieving an enrollment from the database.
     *
     * @param data         Takes an JSON String with one key 'net_id' and as value the UserId.
     * @param sessionToken Takes the session-jws that the client
     *                     received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the enrollments
     *     related to the provided userId in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getEnrollmentByUser")
    public ResponseEntity<String> getEnrollmentByUser(@RequestBody String data,
                                                      @RequestHeader(Constants.SESSIONHEADERKEY)
                                                          String sessionToken) {
        return enrollmentService
            .getEnrollmentByUser(new JSONObject(data).getString("net_id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices to
     * the logic of retrieving an enrollment from the database.
     *
     * @param data         Takes an JSON String with one key 'id' and as value the courseId. and
     *                     a key 'net_id' with as value the userId.
     * @param sessionToken Takes the session-jws that the client
     *                     received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the enrollments
     *     related to the provided userId in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/getEnrollmentByCourseAndUser")
    public ResponseEntity<String> getEnrollmentByCourseAndUser(
        @RequestBody String data, @RequestHeader(Constants.SESSIONHEADERKEY)
                                                                   String sessionToken) {
        return enrollmentService.getEnrollmentByCourseAndUser(new JSONObject(data).getInt("id"),
            new JSONObject(data).getString("net_id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of adding an enrollment to the database.
     *
     * @param data         Takes a JSON String representation of an enrollment to add.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/addEnrollment")
    public ResponseEntity<String> addEnrollment(@RequestBody String data,
                                                @RequestHeader(Constants.SESSIONHEADERKEY)
                                                    String sessionToken) {
        return enrollmentService
            .addEnrollment((Enrollment) serializer.deserialize(data, Enrollment.class),
                sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of updating a enrollment in the database.
     *
     * @param data         Takes a JSON String representation of an enrollment to overwrite.
     *                     This data has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/updateEnrollment")
    public ResponseEntity<String> updateEnrollment(@RequestBody String data,
                                                   @RequestHeader(Constants.SESSIONHEADERKEY)
                                                       String sessionToken) {
        Enrollment enrollment = (Enrollment) serializer.deserialize(data, Enrollment.class);
        return enrollmentService
            .updateEnrollment(enrollment, sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of deleting a question from the database.
     *
     * @param data         Takes an JSON String with one key 'id' and as value the enrollmentId.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/deleteEnrollment")
    public ResponseEntity<String> deleteEnrollment(@RequestBody String data,
                                                   @RequestHeader(Constants.SESSIONHEADERKEY)
                                                       String sessionToken) {
        return enrollmentService.deleteEnrollment(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of deleting a enrollment from the database.
     *
     * @param data         Takes an JSON String with one key 'net_id' and as value the userId. and
     *                     *              a key 'id' with as value the courseId.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/deleteTopicByUserAndCourse")
    public ResponseEntity<String> deleteEnrollmentByUserAndCourse(
        @RequestBody String data, @RequestHeader(Constants.SESSIONHEADERKEY)
                                                                      String sessionToken) {
        return enrollmentService
            .deleteEnrollmentByUserAndCourse(new JSONObject(data).getString("net_id"),
                new JSONObject(data).getInt("id"), sessionToken);
    }
}