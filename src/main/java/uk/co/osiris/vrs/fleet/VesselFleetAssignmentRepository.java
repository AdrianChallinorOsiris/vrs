package uk.co.osiris.vrs.fleet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VesselFleetAssignmentRepository extends JpaRepository<VesselFleetAssignment, Long> {

    @Query("SELECT a FROM VesselFleetAssignment a WHERE a.fleet.id = :fleetId AND a.validTo IS NULL")
    List<VesselFleetAssignment> findCurrentByFleetId(Long fleetId);

    Optional<VesselFleetAssignment> findByFleetIdAndVesselIdAndValidToIsNull(Long fleetId, Long vesselId);
}
