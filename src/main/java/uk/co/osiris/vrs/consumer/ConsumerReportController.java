package uk.co.osiris.vrs.consumer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/v1/consumer-reports")
@RequiredArgsConstructor
@Tag(name = "Consumer Reports", description = "Consumer report configuration and delivery")
public class ConsumerReportController {

    private final ConsumerReportService consumerReportService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "List consumer reports for a fleet")
    public List<ConsumerReportDto> listByFleet(@RequestParam Long fleetId) {
        return consumerReportService.findByFleet(fleetId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get consumer report by ID")
    public ResponseEntity<ConsumerReportDto> getById(@PathVariable Long id) {
        ConsumerReportDto report = consumerReportService.findById(id);
        return report != null ? ResponseEntity.ok(report) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'FLEET_MANAGER')")
    @Operation(summary = "Create a consumer report")
    public ResponseEntity<ConsumerReportDto> create(@Valid @RequestBody ConsumerReportRequestDto request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(consumerReportService.create(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'FLEET_MANAGER')")
    @Operation(summary = "Update a consumer report")
    public ResponseEntity<ConsumerReportDto> update(@PathVariable Long id,
                                                    @Valid @RequestBody ConsumerReportRequestDto request) {
        try {
            return ResponseEntity.ok(consumerReportService.update(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'FLEET_MANAGER')")
    @Operation(summary = "Delete a consumer report")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            consumerReportService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/subscriptions")
    @PreAuthorize("hasAnyRole('MANAGER', 'FLEET_MANAGER')")
    @Operation(summary = "Add a delivery subscription to a consumer report")
    public ResponseEntity<Void> addSubscription(@PathVariable Long id,
                                                @Valid @RequestBody ConsumerSubscriptionRequestDto request) {
        try {
            consumerReportService.addSubscription(id, request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
