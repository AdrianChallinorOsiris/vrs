package uk.co.osiris.vrs.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldTypeRepository extends JpaRepository<FieldType, Long> {

    Optional<FieldType> findByName(String name);
}
