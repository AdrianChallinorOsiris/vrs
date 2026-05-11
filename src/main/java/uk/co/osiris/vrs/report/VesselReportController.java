package uk.co.osiris.vrs.report;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vessel/v1/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VESSEL')")
@Tag(name = "Vessel Reports", description = "Report submission by vessel masters")
public class VesselReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "Submit a report (master only — vessel ID from JWT)")
    public ResponseEntity<ReportDto> submit(@Valid @RequestBody ReportSubmitRequestDto request,
                                            Authentication authentication) {
        try {
            Long vesselId = extractVesselId(authentication);
            if (vesselId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            ReportDto report = reportService.submit(vesselId, request, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Long extractVesselId(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken token
                && token.getDetails() instanceof Claims claims) {
            return claims.get("vesselId", Long.class);
        }
        return null;
    }
}
