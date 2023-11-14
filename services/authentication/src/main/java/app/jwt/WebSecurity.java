package app.jwt;

import app.constants.Constants;
import app.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final transient UserDetailsServiceImpl userDetailsService;
    private final transient BCryptPasswordEncoder cryptPasswordEncoder;

    /**
     * Constructs a WebSecurity.
     *
     * @param userDetailsService   an object of type UserDetailsServiceImpl
     * @param cryptPasswordEncoder an object of type BCryptPasswordEncoder
     */
    @Autowired
    public WebSecurity(UserDetailsServiceImpl userDetailsService,
                       BCryptPasswordEncoder cryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.cryptPasswordEncoder = cryptPasswordEncoder;
    }

    /**
     * In this method we can define which resources are public and which are secured.
     * In our case, we set the SIGN_UP_URL endpoint as being public
     * and everything else as being secured.
     *
     * @param http an http that allows configuring web based security for specific http requests
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, Constants.SIGN_UP_URL).permitAll()
            .antMatchers("/", "/index", "css/*", "/js/*").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * In this method we can define a custom implementation of UserDetailsService to
     * load user-specific data in the security framework.
     * We have also used this method to set the encrypt method
     * used by our application (BCryptPasswordEncoder).
     *
     * @param auth an object of type AuthenticationManagerBuilder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(cryptPasswordEncoder);
    }

}
