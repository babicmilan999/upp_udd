package upp_udd.project.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import upp_udd.project.dto.UserDto;
import upp_udd.project.dto.UserRegistrationDto;
import upp_udd.project.model.ScientificField;
import upp_udd.project.model.User;
import upp_udd.project.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MagazineService magazineService;

    public UserService(UserRepository userRepository, @Lazy MagazineService magazineService) {
        this.userRepository = userRepository;
        this.magazineService = magazineService;
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                                                      user.getPassword(),
                                                                      Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }

    @Transactional(readOnly = true)
    public Map<User.Role, List<UserDto>> getReviewersForScientificFields(Long magazineId) {
        return userRepository.findAllByScientificFieldsAndRoleIn(magazineService.findById(magazineId).getScientificFields().stream().map(ScientificField::getValue).collect(
                Collectors.toSet()))
                                                .stream()
                                                .map(this::map)
                                                .collect(Collectors.groupingBy(UserDto::getRole, Collectors.toList()));
    }

    private UserDto map(User user) {
        return UserDto.builder()
                      .id(user.getId())
                      .username(user.getUsername())
                      .scientificFields(user.getScientificFields())
                      .role(user.getRole())
                      .build();

    }
}
