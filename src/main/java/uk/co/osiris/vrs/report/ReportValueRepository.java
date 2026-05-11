package uk.co.osiris.vrs.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportValueRepository extends JpaRepository<ReportValue, Long> {

    List<ReportValue> findByReportId(Long reportId);
}
