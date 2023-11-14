package app.controllers;

import app.services.AuthorisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorisation")
public class AuthorisationController {

    @Autowired
    private transient AuthorisationService authorisationService;

    /**
     * This method checks if the session has not expired
     * and the authentication level it high enough.
     *
     * @param jwtToken     the jwt session token that gets passed around.
     * @param requiredRole the required role for a certain API call.
     * @return A responseEntity, containing a boolean in JSON format.
     */
    @GetMapping("/getAuthorisation/{requiredRole}")
    public ResponseEntity<String> getAuthorisation(@RequestHeader("session") String jwtToken,
                                                   @PathVariable Integer requiredRole) {

        return authorisationService.verifyAuthentication(jwtToken, requiredRole);
    }
}
