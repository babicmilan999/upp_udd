package upp_udd.project.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import upp_udd.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByHash(String hash);

    User findByUsername(String username);

    @Query(value = " SELECT distinct u.* "
                + " FROM users u join user_scientific_field usf on u.id = usf.user_id join scientific_field sf on usf.scientific_field_id = sf.id "
                + " WHERE sf.value in ('Programming', 'Moleraj') and u.role = 'REVIEWER' "
                + " UNION "
                + " SELECT * "
                + " FROM users "
                + " WHERE role = 'EDITOR' ", nativeQuery = true)
    List<User> findAllByScientificFieldsAndRoleIn(Set<String> scientificFields);

}
