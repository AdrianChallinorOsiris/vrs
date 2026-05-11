package uk.co.osiris.vrs.consumer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.osiris.vrs.report.ReportDto;
import uk.co.osiris.vrs.report.ReportService;
import uk.co.osiris.vrs.vessel.Vessel;
import uk.co.osiris.vrs.vessel.VesselRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pull API", description = "Pull API for external consumers — authenticate with ApiKey header")
public class PullApiController {

    private final ConsumerSubscriptionRepository subscriptionRepository;
    private final ConsumerReportRepository consumerReportRepository;
    private final ReportService reportService;
    private final DeliveryAuditLogRepository auditLogRepository;
    private final VesselRepository vesselRepository;

    @GetMapping("/{consumerReportId}")
    @Operation(summary = "Pull report data for a consumer report — requires ApiKey header")
    public ResponseEntity<List<ReportDto>> pull(
            @PathVariable Long consumerReportId,
            @RequestParam Long vesselId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @Parameter(description = "API key for authentication") @RequestHeader("ApiKey") String apiKey) {

        if (!isValidApiKey(consumerReportId, apiKey)) {
            log.warn("Invalid API key attempt for consumer report {}", consumerReportId);
            return ResponseEntity.status(401).build();
        }

        LocalDateTime effectiveFrom = from != null ? from : LocalDateTime.now().minusDays(30);
        LocalDateTime effectiveTo = to != null ? to : LocalDateTime.now();
        List<ReportDto> reports = reportService.query(vesselId, null, effectiveFrom, effectiveTo);

        writeAuditLog(consumerReportId, vesselId, "SUCCESS", null);
        return ResponseEntity.ok(reports);
    }

    private boolean isValidApiKey(Long consumerReportId, String apiKey) {
        return subscriptionRepository.findByConsumerReportIdAndActiveTrue(consumerReportId).stream()
                .filter(s -> "PULL_API".equals(s.getDeliveryMethod()))
                .anyMatch(s -> {
                    String config = s.getConfig();
                    return config != null && config.contains("\"webhook_key\":\"" + apiKey + "\"");
                });
    }

    private void writeAuditLog(Long consumerReportId, Long vesselId, String status, String error) {
        try {
            ConsumerReport report = consumerReportRepository.findById(consumerReportId).orElse(null);
            Vessel vessel = vesselRepository.findById(vesselId).orElse(null);
            if (report == null || vessel == null) return;
            DeliveryAuditLog entry = new DeliveryAuditLog();
            entry.setConsumerReport(report);
            entry.setVessel(vessel);
            entry.setDeliveryMethod("PULL_API");
            entry.setStatus(status);
            entry.setErrorMessage(error);
            auditLogRepository.save(entry);
        } catch (Exception e) {
            log.warn("Failed to write audit log entry: {}", e.getMessage());
        }
    }
}
