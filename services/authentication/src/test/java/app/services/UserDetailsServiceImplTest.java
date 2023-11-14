package app.services;

import static org.mockito.Mockito.doReturn;

import app.constants.Constants;
import app.models.ApplicationUser;
import app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserDetailsServiceImpl.class)
class UserDetailsServiceImplTest {

    private transient ApplicationUser user;
    private transient User userResult;
    private transient ApplicationUser userNotAuth;
    private transient User userResultNoAuth;
    private transient List<GrantedAuthority> list;
    private transient List<GrantedAuthority> listNoAuth;
    private transient UserDetailsServiceImpl userDetailsService;


    @Autowired
    private transient MockMvc mockMvc;

    @MockBean
    private transient UserRepository userRepository;

    @BeforeEach
    void setup() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
        user = new ApplicationUser();
        userNotAuth = new ApplicationUser();
        user.setNetId("1");
        user.setPassword("hello");
        user.setRole(1);
        user.setExtraTime(1);

        userNotAuth.setNetId("1");
        userNotAuth.setPassword("hello");
        userNotAuth.setRole(0);
        userNotAuth.setExtraTime(1);
        list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(user.getRole().toString()));
        listNoAuth = new ArrayList<>();
        userResult = new User(user.getNetId(), user.getPassword(), list);
        userResultNoAuth = new User(userNotAuth.getNetId(), userNotAuth.getPassword(), listNoAuth);
    }


    @Test
    void loadUserByUsername() {
        doReturn(user).when(userRepository).findByNetId(Constants.TEST);
        UserDetails userDetails = userDetailsService.loadUserByUsername(Constants.TEST);
        Assertions.assertEquals(userResult, userDetails);
    }

    @Test
    void loadUserByUsernameNull() throws UsernameNotFoundException {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("test");
        });

    }

    @Test
    void loadUserByUsernameNoAuth() {
        doReturn(userNotAuth).when(userRepository).findByNetId(Constants.TEST);
        UserDetails userDetails = userDetailsService.loadUserByUsername(Constants.TEST);
        Assertions.assertEquals(userResultNoAuth, userDetails);
    }

    @Test
    void loadUserByUsernameRole() {
        doReturn(user).when(userRepository).findByNetId(Constants.TEST);
        UserDetails userDetails = userDetailsService.loadUserByUsername(Constants.TEST);
        Assertions.assertEquals(1, userDetails.getAuthorities().size());
    }
}