package app.controllers;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.constants.Constants;
import app.services.AuthorisationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthorisationController.class)
class AuthorisationControllerTest {

    @Autowired
    private transient MockMvc mockMvc;
    @MockBean
    private transient AuthorisationService authorisationService;

    @Test
    void getAuthorisation() {
        try {
            mockMvc.perform(get("/authorisation//getAuthorisation/1")
                .header(Constants.SESSIONHEADERKEY, Constants.SESSION_ID_STUDENT)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        } catch (Exception exception) {
            fail("The test threw an exception");
        }
    }
}