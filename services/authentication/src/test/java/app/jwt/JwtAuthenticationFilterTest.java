package app.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class JwtAuthenticationFilterTest {

    @Test
    public void successfulAuthenticationStudent() {
        String netId = "netIdStudent";
        Integer role = 0;
        String tokenString = Constants.SESSION_ID_STUDENT;

        Jws<Claims> tokenObject = Jwts.parserBuilder()
            .requireSubject("session")
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(tokenString);

        assertEquals(netId, tokenObject.getBody().get("netId"));
        assertEquals(role, tokenObject.getBody().get("role"));
        assertTrue(new Date().compareTo(tokenObject.getBody().getExpiration()) < 0);
    }

    @Test
    public void successfulAuthenticationTeacher() {
        String netId = "netIdTeacher";
        Integer role = 1;
        String tokenString = Constants.SESSION_ID_TEACHER;

        Jws<Claims> tokenObject = Jwts.parserBuilder()
            .requireSubject("session")
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(tokenString);

        assertEquals(netId, tokenObject.getBody().get("netId"));
        assertEquals(role, tokenObject.getBody().get("role"));
        assertTrue(new Date().compareTo(tokenObject.getBody().getExpiration()) < 0);
    }

    @Test
    public void successfulAuthenticationEmptyValues() {
        String netId = "";
        Integer role = 0;
        String tokenString = Constants.SESSION_ID_EMPTY;

        Jws<Claims> tokenObject = Jwts.parserBuilder()
            .requireSubject("session")
            .setSigningKey(Constants.SECRET_KEY)
            .build()
            .parseClaimsJws(tokenString);

        assertEquals(netId, tokenObject.getBody().get("netId"));
        assertEquals(role, tokenObject.getBody().get("role"));
        assertTrue(new Date().compareTo(tokenObject.getBody().getExpiration()) < 0);
    }
}