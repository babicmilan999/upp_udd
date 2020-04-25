package upp_udd.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import upp_udd.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByHash(String hash);

    User findByUsername(String username);

}
