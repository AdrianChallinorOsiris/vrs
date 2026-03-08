package uk.co.osiris.vrs.company;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/v1/companies")
@RequiredArgsConstructor
@Tag(name = "User Company", description = "Company read and search endpoints")
public class UserCompanyController {

	private final CompanyService companyService;

	@GetMapping("/{id}")
	@Operation(summary = "Get company by ID")
	public ResponseEntity<CompanyDto> getById(@PathVariable Long id) {
		CompanyDto company = companyService.findById(id);
		return company != null
				? ResponseEntity.ok(company)
				: ResponseEntity.notFound().build();
	}

	@GetMapping
	@Operation(summary = "Search companies by name")
	public List<CompanyDto> searchByName(@RequestParam String name) {
		return companyService.searchByName(name);
	}
}
