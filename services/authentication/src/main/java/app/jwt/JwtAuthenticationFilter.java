package app.jwt;

import app.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final transient AuthenticationManager authenticationManager;

    /**
     * Constructs a JwtAuthenticationFilter.
     *
     * @param authenticationManager the authentication request object
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * This method is to parse the user's credentials and issue them to the AuthenticationManager.
     *
     * @param request  HttpRequest for the server.
     * @param response HttpResponse for the server.
     * @return an user if the credentials are valid
     * @throws AuthenticationException if the credentials are invalid
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
        throws AuthenticationException {

        try {

            UsernameAndPasswordAuthRequest authenticationRequest = new ObjectMapper()
                .readValue(request.getInputStream(), UsernameAndPasswordAuthRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getNetId(),
                authenticationRequest.getPassword()
            );

            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method is called when a user successfully logs in.
     * We use this method to generate a JWT for this user.
     *
     * @param req        HttpRequest for the server.
     * @param res        HttpResponse for the server.
     * @param chain      object provided by the servlet container to the developer
     *                   giving a view into the invocation chain of a
     *                   filtered request for a resource.
     * @param authResult Represents the token for an authentication request or for an
     *                   authenticated principal once the request has been processed by the
     *                   {@link AuthenticationManager#authenticate(Authentication)} method.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res, FilterChain chain,
                                            Authentication authResult) {


        String netId = authResult.getName();
        Integer role = authResult.getAuthorities().size();

        String token = Jwts.builder()
            // informs the receiver what this jwt is about (header)
            .setSubject("session")
            // informs the receiver for what final date and time it should check (header)
            .setExpiration(Date.valueOf(LocalDate.now().plusDays(1)))
            // represents the unique session-Id (header)
            .setId(UUID.randomUUID().toString())
            // provides the corresponding nedId (body)
            .claim("netId", netId)
            // provides the corresponding role (body)
            .claim("role", role)
            // signs the jwt using a HMAC algorithm using SHA-256. A symmetric algorithm has been
            // chosen over an asymmetric one since it better suits the microservices architecture.
            .signWith(Constants.SECRET_KEY, SignatureAlgorithm.HS512)
            // 'builds' the actual jws.
            .compact();

        res.addHeader(Constants.HEADER_STRING, token);
    }
}
