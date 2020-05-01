package upp_udd.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp_udd.project.model.Magazine;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    List<Magazine> findAllByStatus(Magazine.Status status);

}
