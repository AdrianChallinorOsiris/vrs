package uk.co.osiris.vrs.company;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.osiris.vrs.users_roles.UserAccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

	private final CompanyRepository companyRepository;
	private final CompanyMapper companyMapper;
	private final UserAccountRepository userAccountRepository;

	@Transactional(readOnly = true)
	public CompanyDto findByUserEmail(String email) {
		var userAccount = userAccountRepository.findByEmail(email);
		if (userAccount == null || userAccount.getCompany() == null) {
			return null;
		}
		return companyMapper.toDto(userAccount.getCompany());
	}

	@Transactional(readOnly = true)
	public CompanyDto findById(Long id) {
		return companyRepository.findById(id)
				.map(companyMapper::toDto)
				.orElse(null);
	}

	@Transactional(readOnly = true)
	public List<CompanyDto> searchByName(String name) {
		return companyRepository.findByNameContainingIgnoreCase(name).stream()
				.map(companyMapper::toDto)
				.toList();
	}

	@Transactional
	public CompanyDto create(CompanyRequestDto request) {
		Company company = companyMapper.toEntity(request);
		company = companyRepository.save(company);
		log.info("Created company: {}", company.getName());
		return companyMapper.toDto(company);
	}

	@Transactional
	public CompanyDto update(Long id, CompanyRequestDto request) {
		Company company = companyRepository.findById(id)
				.orElse(null);
		if (company == null) {
			return null;
		}
		companyMapper.toEntity(request, company);
		company = companyRepository.save(company);
		log.info("Updated company: {}", company.getName());
		return companyMapper.toDto(company);
	}

	@Transactional
	public boolean delete(Long id) {
		if (!companyRepository.existsById(id)) {
			return false;
		}
		companyRepository.deleteById(id);
		log.info("Deleted company with id: {}", id);
		return true;
	}
}
