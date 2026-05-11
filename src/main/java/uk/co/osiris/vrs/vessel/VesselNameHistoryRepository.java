package uk.co.osiris.vrs.vessel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VesselNameHistoryRepository extends JpaRepository<VesselNameHistory, Long> {

    List<VesselNameHistory> findByVesselIdOrderByValidFromDesc(Long vesselId);

    Optional<VesselNameHistory> findByVesselIdAndValidToIsNull(Long vesselId);
}
