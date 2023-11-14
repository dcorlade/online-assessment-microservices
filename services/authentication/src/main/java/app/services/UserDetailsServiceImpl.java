package app.services;

import app.constants.Constants;
import app.models.ApplicationUser;
import app.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final transient UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByNetId(username);
        if (user == null) {
            throw new UsernameNotFoundException("NetId not found");
        }
        List<GrantedAuthority> l = new ArrayList<>();

        if (user.getRole() == Constants.ROLE_NUMBER) {
            l.add(new SimpleGrantedAuthority(user.getRole().toString()));
        }
        return new User(user.getNetId(), user.getPassword(), l);
    }
}
