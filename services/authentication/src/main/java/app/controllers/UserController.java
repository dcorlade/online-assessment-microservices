package app.controllers;

import app.models.ApplicationUser;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
class UserController {

    private final transient UserRepository userRepository;
    private final transient BCryptPasswordEncoder cryptPasswordEncoder;

    /**
     * Constructs a UserController.
     *
     * @param userRepository       Takes a UserRepository to assign to its instance variable.
     *                             The constructor is Autowired, meaning that Spring will take
     *                             the instance that is has and assign it to the variable
     *                             automatically.
     * @param cryptPasswordEncoder Takes a BCryptPasswordEncoder to assign to its instance
     *                             variable.The constructor is Autowired, meaning that Spring
     *                             will take the instance that is has and assign it to the
     *                             variable automatically.
     */
    @Autowired
    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder cryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.cryptPasswordEncoder = cryptPasswordEncoder;
    }

    /**
     * This method registers an user, encrypts the password of the new user
     * and then saves it to the database.
     * The encryption process is handled by an instance of BCryptPasswordEncoder,
     * which is a class that belongs to the Spring Security framework.
     *
     * @param user The user object to be added as a @RequestBody.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody ApplicationUser user) {
        user.setPassword(cryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<>("Successful signup!", HttpStatus.OK);
    }

}
