package app.services;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Course;
import app.repositories.CourseRepository;
import app.serializerfactory.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private transient CourseRepository courseRepository;

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    /**
     * Constructs a CourseController.
     *
     * @param courseRepository Takes a courseRepository to assign to its instance variable.
     *                         The constructor is Autowired, meaning that Spring will take the
     *                         instance that is has and assign it to the variable automatically.
     */
    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * First checks if the user is allowed to perform the action.
     * Then checks if there is not already
     * a course present in the database. Only then it adds the course.
     *
     * @param course       The course object to be added.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    public ResponseEntity<String> addCourse(Course course, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        courseRepository.save(course);
        return new ResponseEntity<String>(serializer.serialize(course), HttpStatus.OK);
    }

    /**
     * First checks if the user is allowed to perform the action. Then checks if there is a course
     * already present in the database. Only then it updates the course.
     *
     * @param course       The course object to overwrite the current course.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    public ResponseEntity<String> updateCourse(Course course, String sessionToken) {
        try {
            if (!Authorisation.getAuthorisation(sessionToken, 1)) {
                String errorMessage = Constants.NOT_AUTHORIZED_STRING;
                return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
            }
            if (courseRepository.findByCourseCode(course.getCourseCode()).isEmpty()) {
                throw new Exception();
            } else {
                courseRepository.save(course);
                return new ResponseEntity<String>("Update was succesful", HttpStatus.OK);
            }

        } catch (Exception exception) {
            String errorMessage = "Could not update an existing course";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.ALREADY_REPORTED);
        }
    }

    /**
     * First checks if the user is allowed to perform the action. Then tries to retrieve the course
     * from the database.
     *
     * @param courseId     The primary key of the course to retrieve it with.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request,
     *                     with 'session' as key.
     * @return a responseEntity containing the course in case
     *     of success and an error-message otherwise.
     */
    public ResponseEntity<String> getCourse(Integer courseId, String sessionToken) {
        try {
            if (!Authorisation.getAuthorisation(sessionToken, 1)) {
                String errorMessage = Constants.NOT_AUTHORIZED_STRING;
                return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
            }

            Course course = courseRepository.findById(courseId).orElseThrow();
            return new ResponseEntity<String>(serializer.serialize(course), HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "Could not get the course";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * First checks if the user is allowed to perform the action. Then checks if there is a course
     * present in the database. Only then it deletes the course.
     *
     * @param course       The course object to delete.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return a responseEntity containing a statuscode and a  success-message in case of success,
     *     and an error-message otherwise.
     */
    public ResponseEntity<String> deleteCourse(Course course, String sessionToken) {

        try {
            if (!Authorisation.getAuthorisation(sessionToken, 1)) {
                String errorMessage = Constants.NOT_AUTHORIZED_STRING;
                return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
            }

            if (courseRepository.findByCourseCode(course.getCourseCode()).isEmpty()) {
                throw new Exception();
            }

            courseRepository.delete(course);
            return new ResponseEntity<String>("Deletion was succesful", HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "Could not delete the course";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.EXPECTATION_FAILED);
        }
    }
}

