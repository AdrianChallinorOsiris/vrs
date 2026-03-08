package uk.co.osiris.vrs.company;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Admin Company", description = "Company management endpoints")
public class AdminCompanyController {

	private final CompanyService companyService;

	@PostMapping
	@Operation(summary = "Create a company")
	public ResponseEntity<CompanyDto> create(@Valid @RequestBody CompanyRequestDto request) {
		CompanyDto created = companyService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a company")
	public ResponseEntity<CompanyDto> update(@PathVariable Long id, @Valid @RequestBody CompanyRequestDto request) {
		CompanyDto updated = companyService.update(id, request);
		return updated != null
				? ResponseEntity.ok(updated)
				: ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a company")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return companyService.delete(id)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
