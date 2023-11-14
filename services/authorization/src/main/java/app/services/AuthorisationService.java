package app.services;

import app.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationService {

    /**
     * This method verifies whether a certain action is allowed or not. For now, it only verifies
     * whether the role corresponds to the required role, and whether the session is not expired.
     * However, the method is created in a scalable manner to take future expansion into account.
     *
     * @param jwtString    The jwt session information received from the requester.
     * @param requiredRole The required rol that the requester specified.
     * @return returns true is the requested action is allowed. Else, return false.
     */
    public ResponseEntity<String> verifyAuthentication(String jwtString, Integer requiredRole) {
        try {
            Jws<Claims> jwtObject = receiveSession(jwtString);

            Integer role = getSessionRole(jwtObject);

            if (role >= requiredRole) {
                return new ResponseEntity<String>(
                    new JSONObject().put("authorised", true).toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>(
                    new JSONObject().put("authorised", false).toString(), HttpStatus.OK);
            }
        } catch (JwtException exception) {
            return new ResponseEntity<String>(
                new JSONObject().put("authorised", false).toString(), HttpStatus.OK);
        }
    }

    /**
     * This method parsed the jws-String into an object that can be accessed.
     * It throws an exception when the signature
     * cannot be verified. In that case, the calling class
     * should try pulling a new String a few more times,
     * before concluding that that something is wrong.
     *
     * @param jwsString A jws String to be parsed.
     * @return a Jws object from which you can access the Session Information.
     * @throws JwtException when this method throws an exception,
     *                      the signature could not be
     *                      verified and someone might have tampered with the jws.
     */
    private Jws<Claims> receiveSession(String jwsString) throws JwtException {
        return Jwts.parserBuilder()
            // asserts that the subject should have been set to "session"
            .requireSubject("session")
            // Provides the key to decode the jws, which should be the same as the encoding key.
            .setSigningKey(Constants.SECRET_KEY)
            // 'builds' the object
            .build()
            //converts the jwsString into an jwt object
            .parseClaimsJws(jwsString);
    }

    /**
     * This method provides a small abstraction to get the user role from the jwsObject.
     *
     * @param jwsObject the object that has been parsed out of the string send by the user.
     * @return the role of the user in Integer format.
     */
    private Integer getSessionRole(Jws<Claims> jwsObject) {
        return jwsObject.getBody().get("role", Integer.class);
    }
}




