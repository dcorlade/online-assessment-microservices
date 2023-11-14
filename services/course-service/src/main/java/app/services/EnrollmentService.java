package app.services;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Enrollment;
import app.repositories.EnrollmentRepository;
import app.serializerfactory.Serializer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * This class contains all logic for the endpoints found in EnrollmentController.
 * Every method will check whether or not the user is authorized and then perform some logic after
 * which it returns a ResponseEntity.
 */
@Service
public class EnrollmentService {

    private final transient EnrollmentRepository enrollmentRepository;
    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Given the enrollmentId this method will return a ResponseEntity
     * containing a string representation of the Enrollment object.
     *
     * @param enrollmentId id of the enrollment to be returned
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string representation of the enrollment
     */
    public ResponseEntity<String> getEnrollment(Integer enrollmentId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        if (enrollmentRepository.findById(enrollmentId).isEmpty()) {
            String errorMessage = Constants.NO_ENROLLMENT;
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        return new ResponseEntity<String>(serializer.serialize(enrollment), HttpStatus.OK);
    }

    /**
     * Given a courseId this method returns a ResponseEntity
     * containing a string representation of
     * a JSONObject containing a JSONArray containing json
     * representations of all enrollments with that
     * courseId.
     *
     * @param courseId     Integer of the courseId
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string
     *     representation of the enrollments in JSONArray format
     */
    public ResponseEntity<String> getEnrollmentByCourse(Integer courseId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        Object[] enrollments = enrollmentRepository.findAll().stream()
            .filter(enrollment -> enrollment.getCourseId() == courseId).toArray();

        if (enrollments.length == 0) {
            String errorMessage = "There exist no enrollments with that courseId";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }

        JSONArray jsonArrayEnrollments = new JSONArray();
        for (Object e : enrollments) {
            Enrollment enrollment = (Enrollment) e;
            jsonArrayEnrollments.put(serializer.serialize(enrollment));
        }
        JSONObject jsonObjectEnrollments = new JSONObject();
        jsonObjectEnrollments.put("enrollments", jsonArrayEnrollments);
        return new ResponseEntity<String>(jsonObjectEnrollments.toString(), HttpStatus.OK);
    }

    /**
     * Given a netId this method returns a ResponseEntity
     * containing a string representation of
     * a JSONObject containing a JSONArray containing json
     * representations of all enrollments with that
     * netId.
     *
     * @param netId        String of netId
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity containing a string
     *     representation of the enrollments in JSONArray format
     */
    public ResponseEntity<String> getEnrollmentByUser(String netId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }


        Object[] enrollments = enrollmentRepository.findAll().stream()
            .filter(enrollment -> enrollment.getUserId().contentEquals(netId)).toArray();

        if (enrollments.length == 0) {
            String errorMessage = "There exist no enrollments with that userId";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }

        JSONArray jsonArrayEnrollments = new JSONArray();
        for (Object e : enrollments) {
            Enrollment enrollment = (Enrollment) e;
            jsonArrayEnrollments.put(serializer.serialize(enrollment));
        }
        JSONObject jsonObjectEnrollments = new JSONObject();
        jsonObjectEnrollments.put("enrollments", jsonArrayEnrollments);
        return new ResponseEntity<String>(jsonObjectEnrollments.toString(), HttpStatus.OK);
    }

    /**
     * Given an Enrollment object it will attempt to store this in the database.
     * If an enrollment with the
     * same id or with the same netId and courseId already exists it will not store it.
     *
     * @param enrollment   Enrollment object to be added
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity
     */
    public ResponseEntity<String> addEnrollment(Enrollment enrollment,
                                                String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        Jws<Claims> jwsObject = Jwts.parserBuilder()
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(sessionToken);
        String netId = jwsObject.getBody().get(Constants.NET_ID_KEY, String.class);
        if (!netId.contentEquals(enrollment.getUserId())) {
            return new ResponseEntity<String>("You can only enroll yourself", HttpStatus.FORBIDDEN);
        }
        if (!enrollmentRepository
            .findEnrollmentByCourseIdAndUserId(enrollment.getCourseId(), enrollment.getUserId())
            .isEmpty()) {
            String errorMessage = "There all ready exists an enrollment with that id";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.CONFLICT);
        }
        enrollmentRepository.save(enrollment);
        return new ResponseEntity<String>("Add was successful", HttpStatus.OK);

    }

    /**
     * Given an Enrollment object it will attempt to update this in the database.
     * If no enrollment with the
     * id exists it will return 404 NOT_FOUND.
     *
     * @param enrollment   Enrollment object to be updated
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity
     */
    public ResponseEntity<String> updateEnrollment(Enrollment enrollment,
                                                   String sessionToken) {
        Jws<Claims> jwsObject = Jwts.parserBuilder()
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(sessionToken);
        String netId = jwsObject.getBody().get(Constants.NET_ID_KEY, String.class);
        if (!Authorisation.getAuthorisation(sessionToken, 0)
            || !netId.contentEquals(enrollment.getUserId())) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        if (enrollmentRepository.findById(enrollment.getId()).isEmpty()) {
            String errorMessage = Constants.NO_ENROLLMENT;
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
        enrollmentRepository.save(enrollment);
        return new ResponseEntity<String>("Update was successful", HttpStatus.OK);
    }

    /**
     * Given an enrollmentId this method will attempt to
     * remove the related enrollment from the database.
     * If no enrollment with that id exists 404 NOT_FOUND will be returned.
     *
     * @param enrollmentId id of the enrollment to be deleted.
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity
     */
    public ResponseEntity<String> deleteEnrollment(Integer enrollmentId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        if (enrollmentRepository.findById(enrollmentId).isEmpty()) {
            String errorMessage = Constants.NO_ENROLLMENT;
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }

        Jws<Claims> jwsObject = Jwts.parserBuilder()
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(sessionToken);
        String netId = jwsObject.getBody().get(Constants.NET_ID_KEY, String.class);

        if (!netId
            .contentEquals(enrollmentRepository.findById(enrollmentId).get().getUserId())) {
            return new ResponseEntity<String>("You can only delete your own enrollments",
                HttpStatus.FORBIDDEN);
        }

        enrollmentRepository.deleteById(enrollmentId);
        return new ResponseEntity<String>("Deletion was successful", HttpStatus.OK);
    }

    /**
     * Given a netId and a courseId this method will
     * attempt to remove the related enrollment from the database.
     * If no such enrollment exists 404 NOT_FOUND will be returned.
     *
     * @param userId       String netId
     * @param courseId     Integer courseId
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity
     */
    public ResponseEntity<String> deleteEnrollmentByUserAndCourse(String userId, Integer courseId,
                                                                  String sessionToken) {
        Jws<Claims> jwsObject = Jwts.parserBuilder()
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(sessionToken);
        String netId = jwsObject.getBody().get(Constants.NET_ID_KEY, String.class);

        if (!Authorisation.getAuthorisation(sessionToken, 0)
            || !userId.contentEquals(netId)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        Object[] enrollments = enrollmentRepository.findAll().stream()
            .filter(enrollment -> enrollment.getUserId().contentEquals(userId))
            .filter(enrollment -> enrollment.getCourseId() == courseId)
            .map(Enrollment::getId)
            .toArray();

        if (enrollments.length == 0) {
            String errorMessage = "There exists no enrollment with that userId and courseId";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }

        for (Object id : enrollments) {
            enrollmentRepository.deleteById((Integer) id);
        }
        return new ResponseEntity<String>("Deletion was successful", HttpStatus.OK);
    }

    /**
     * Given a netId and a courseId this method will
     * attempt to return the related enrollment from the database.
     * If no such enrollment exists 404 NOT_FOUND will be returned.
     * If multiple such enrollments exist only one will be returned.
     *
     * @param userId       String netId
     * @param courseId     Integer courseId
     * @param sessionToken Takes the session-jws that the client received when logging in. It has
     *                     to be supplied in the header of the HTTP-request, with 'session' as key.
     * @return ResponseEntity
     */
    public ResponseEntity<String> getEnrollmentByCourseAndUser(Integer courseId, String userId,
                                                               String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        List<Enrollment> enrollments =
            enrollmentRepository.findEnrollmentByCourseIdAndUserId(courseId, userId);

        if (enrollments.size() == 0) {
            String errorMessage = Constants.NO_ENROLLMENT;
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(serializer.serialize(enrollments.get(0)), HttpStatus.OK);
    }
}