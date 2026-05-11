package uk.co.osiris.vrs.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportFieldDefinitionRepository extends JpaRepository<ReportFieldDefinition, Long> {

    List<ReportFieldDefinition> findByReportTypeIdOrderByDisplayOrder(Long reportTypeId);
}
