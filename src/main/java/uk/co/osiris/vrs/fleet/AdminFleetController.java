package uk.co.osiris.vrs.fleet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/v1/fleets")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Fleet Admin", description = "Fleet management (admin/manager)")
public class AdminFleetController {

    private final FleetService fleetService;

    @PostMapping
    @Operation(summary = "Create a fleet")
    public ResponseEntity<FleetDto> create(@Valid @RequestBody FleetRequestDto request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(fleetService.create(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a fleet")
    public ResponseEntity<FleetDto> update(@PathVariable Long id, @Valid @RequestBody FleetRequestDto request) {
        try {
            return ResponseEntity.ok(fleetService.update(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a fleet")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            fleetService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{fleetId}/vessels")
    @Operation(summary = "Assign a vessel to a fleet")
    public ResponseEntity<FleetAssignmentDto> assignVessel(@PathVariable Long fleetId,
                                                           @Valid @RequestBody FleetVesselRequestDto request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(fleetService.assignVessel(fleetId, request.getVesselId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{fleetId}/vessels/{vesselId}")
    @Operation(summary = "Remove a vessel from a fleet")
    public ResponseEntity<Void> removeVessel(@PathVariable Long fleetId, @PathVariable Long vesselId) {
        try {
            fleetService.removeVessel(fleetId, vesselId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
