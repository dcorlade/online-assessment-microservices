package app.controllers;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.models.ApplicationUser;
import app.repositories.UserRepository;
import app.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient UserRepository userRepository;
    @MockBean
    private transient BCryptPasswordEncoder cryptPasswordEncoder;
    @MockBean
    private transient UserDetailsServiceImpl userDetailsService;

    private transient ApplicationUser user;

    @BeforeEach
    void init() {
        this.user = new ApplicationUser("testNetId", "testPassword", 0, 0);
    }

    @Test
    public void signUpTest() throws Exception {
        doReturn("hello").when(cryptPasswordEncoder).encode(user.getPassword());
        doReturn(user).when(userRepository).save(user);

        mockMvc.perform(post(("/authentication/sign-up"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(user.toJson())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(content().string("Successful signup!"));
    }
}