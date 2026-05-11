package uk.co.osiris.vrs.fleet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/v1/fleets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@Tag(name = "Fleet", description = "Fleet read endpoints")
public class UserFleetController {

    private final FleetService fleetService;

    @GetMapping
    @Operation(summary = "List all fleets, optionally filtered by company")
    public List<FleetDto> list(@RequestParam(required = false) Long companyId) {
        if (companyId != null) {
            return fleetService.findByCompany(companyId);
        }
        return fleetService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get fleet by ID")
    public ResponseEntity<FleetDto> getById(@PathVariable Long id) {
        FleetDto fleet = fleetService.findById(id);
        return fleet != null ? ResponseEntity.ok(fleet) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/vessels")
    @Operation(summary = "List current vessels in a fleet")
    public ResponseEntity<List<FleetAssignmentDto>> getVessels(@PathVariable Long id) {
        return ResponseEntity.ok(fleetService.findCurrentVessels(id));
    }
}
