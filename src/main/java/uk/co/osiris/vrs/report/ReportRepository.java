package uk.co.osiris.vrs.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByVesselIdAndReportTypeIdAndSubmittedAtBetweenOrderBySubmittedAtDesc(
            Long vesselId, Long reportTypeId, LocalDateTime from, LocalDateTime to);

    List<Report> findByVesselIdAndSubmittedAtBetweenOrderBySubmittedAtDesc(
            Long vesselId, LocalDateTime from, LocalDateTime to);
}
