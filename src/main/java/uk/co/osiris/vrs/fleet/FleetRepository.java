package uk.co.osiris.vrs.fleet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FleetRepository extends JpaRepository<Fleet, Long> {

    List<Fleet> findByCompanyId(Long companyId);
}
