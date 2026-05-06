package uk.co.osiris.vrs.vessel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vessels/v1")
@RequiredArgsConstructor
@Tag(name = "Vessel", description = "Vessel management endpoints")
public class VesselController {

	private final VesselService vesselService;

	@GetMapping
	@Operation(summary = "List all vessels")
	public List<VesselDto> list() {
		return vesselService.findAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get vessel by ID")
	public ResponseEntity<VesselDto> getById(@PathVariable Long id) {
		VesselDto vessel = vesselService.findById(id);
		return vessel != null
				? ResponseEntity.ok(vessel)
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasRole('MANAGER')")
	@Operation(summary = "Create a vessel")
	public ResponseEntity<VesselDto> create(@Valid @RequestBody VesselRequestDto request) {
		try {
			VesselDto created = vesselService.create(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('CHARTERER', 'MANAGER', 'OWNER', 'TECHNICAL_MANAGER')")
	@Operation(summary = "Update vessel (name and active only)")
	public ResponseEntity<VesselDto> update(@PathVariable Long id, @Valid @RequestBody VesselUpdateDto request) {
		try {
			VesselDto updated = vesselService.update(id, request);
			return updated != null
					? ResponseEntity.ok(updated)
					: ResponseEntity.notFound().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
