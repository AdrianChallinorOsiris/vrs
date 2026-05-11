package uk.co.osiris.vrs.vessel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VesselCompanyHistoryRepository extends JpaRepository<VesselCompanyHistory, Long> {

    List<VesselCompanyHistory> findByVesselIdOrderByValidFromDesc(Long vesselId);

    Optional<VesselCompanyHistory> findByVesselIdAndValidToIsNull(Long vesselId);
}
