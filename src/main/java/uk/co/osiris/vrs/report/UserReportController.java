package uk.co.osiris.vrs.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user/v1/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@Tag(name = "Reports", description = "Report query endpoints")
public class UserReportController {

    private final ReportService reportService;

    @GetMapping
    @Operation(summary = "Query reports for a vessel")
    public List<ReportDto> query(
            @RequestParam Long vesselId,
            @RequestParam(required = false) Long reportTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        LocalDateTime effectiveFrom = from != null ? from : LocalDateTime.now().minusDays(30);
        LocalDateTime effectiveTo = to != null ? to : LocalDateTime.now();
        return reportService.query(vesselId, reportTypeId, effectiveFrom, effectiveTo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a report by ID")
    public ResponseEntity<ReportDto> getById(@PathVariable Long id) {
        ReportDto report = reportService.findById(id);
        return report != null ? ResponseEntity.ok(report) : ResponseEntity.notFound().build();
    }

    @GetMapping("/types/{reportTypeId}/fields")
    @Operation(summary = "Get field definitions for a report type")
    public List<ReportFieldDefinitionDto> getFieldDefinitions(@PathVariable Long reportTypeId) {
        return reportService.getFieldDefinitions(reportTypeId);
    }
}
