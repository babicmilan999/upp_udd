package upp_udd.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import upp_udd.project.model.ScientificField;

public interface ScientificFieldRepository extends JpaRepository<ScientificField, Long> {

    ScientificField findByValue(String value);

}
