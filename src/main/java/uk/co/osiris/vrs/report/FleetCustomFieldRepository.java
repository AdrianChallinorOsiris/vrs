package uk.co.osiris.vrs.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FleetCustomFieldRepository extends JpaRepository<FleetCustomField, Long> {

    List<FleetCustomField> findByFleetIdAndReportTypeIdOrderByDisplayOrder(Long fleetId, Long reportTypeId);
}
