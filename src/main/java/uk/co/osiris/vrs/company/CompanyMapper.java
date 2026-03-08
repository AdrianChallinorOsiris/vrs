package uk.co.osiris.vrs.company;

import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

	public CompanyDto toDto(Company company) {
		if (company == null) {
			return null;
		}
		CompanyDto dto = new CompanyDto();
		dto.setId(company.getId());
		dto.setName(company.getName());
		dto.setEmail(company.getEmail());
		return dto;
	}

	public Company toEntity(CompanyRequestDto dto) {
		return toEntity(dto, null);
	}

	public Company toEntity(CompanyRequestDto dto, Company existing) {
		if (dto == null) {
			return null;
		}
		Company company = existing != null ? existing : new Company();
		company.setName(dto.getName());
		company.setEmail(dto.getEmail());
		return company;
	}
}
