package upp_udd.project.services;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import upp_udd.project.model.User;
import upp_udd.project.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                                                      user.getPassword(),
                                                                      Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
