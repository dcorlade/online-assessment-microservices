package app.controllers;

import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Course;
import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;
import app.services.CourseService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courseService/course")
public class CourseController {
    private final transient CourseService courseService;
    private final transient SerializerFactory serializerFactory = new JsonSerializerFactory();
    private final transient Serializer serializer = serializerFactory.createSerializer();

    /**
     * Constructs a CourseController.
     *
     * @param courseService Takes a courseService to assign to its instance variable.
     *                      The constructor
     *                      is Autowired, meaning that Spring will take the instance that is has and
     *                      assign it to the variable automatically.
     */
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of adding a course to the database.
     *
     * @param data         Takes a course to add. This course has to be supplied in the body of the
     *                     HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/createCourse")
    public ResponseEntity<String> addCourse(@RequestBody String data,
                                            @RequestHeader(Constants.SESSIONHEADERKEY)
                                                String sessionToken) {
        return courseService.addCourse((Course)
            serializer.deserialize(data, Course.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices to
     * the logic of retrieving a course from the database.
     *
     * @param data         Takes an JSON with one key 'id' and as value the courseId of the course
     *                     to retrieve.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and the course in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/courseById")
    public ResponseEntity<String> getCourse(@RequestBody String data,
                                            @RequestHeader(Constants.SESSIONHEADERKEY)
                                                String sessionToken) {
        return courseService.getCourse(new JSONObject(data).getInt("id"), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of updating a course in the database.
     *
     * @param data         Takes a course to overwrite the other course with.
     *                     This course has to be supplied in
     *                     the body of the HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/updateCourse")
    public ResponseEntity<String> updateCourse(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        return courseService.updateCourse((Course)
            serializer.deserialize(data, Course.class), sessionToken);
    }

    /**
     * Redirects HTTP-requests from other microservices
     * to the logic of deleting a course from the database.
     *
     * @param data         Takes a course to delete. This course has to be supplied in the body of
     *                     the HTTP-request in JSON format.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and success-message in case of success,
     *     and an error-message otherwise.
     */
    @PostMapping("/deleteCourse")
    public ResponseEntity<String> deleteCourse(@RequestBody String data,
                                               @RequestHeader(Constants.SESSIONHEADERKEY)
                                                   String sessionToken) {
        return courseService
            .deleteCourse((Course) serializer.deserialize(data, Course.class), sessionToken);
    }
}
