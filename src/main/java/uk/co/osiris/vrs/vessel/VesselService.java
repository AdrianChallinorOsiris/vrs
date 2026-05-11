package uk.co.osiris.vrs.vessel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.osiris.vrs.company.Company;
import uk.co.osiris.vrs.company.CompanyRepository;
import uk.co.osiris.vrs.users_roles.UserAccount;
import uk.co.osiris.vrs.users_roles.UserAccountRepository;
import uk.co.osiris.vrs.users_roles.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VesselService {

	private final VesselRepository vesselRepository;
	private final VesselTypeRepository vesselTypeRepository;
	private final VesselMapper vesselMapper;
	private final VesselNameHistoryRepository nameHistoryRepository;
	private final VesselCompanyHistoryRepository companyHistoryRepository;
	private final CompanyRepository companyRepository;
	private final UserAccountRepository userAccountRepository;

	@Transactional(readOnly = true)
	public VesselDto findById(Long id) {
		return vesselRepository.findById(id)
				.map(v -> vesselMapper.toDto(v, nameHistoryRepository.findByVesselIdOrderByValidFromDesc(v.getId())))
				.orElse(null);
	}

	@Transactional(readOnly = true)
	public List<VesselDto> findAll() {
		return vesselRepository.findAll().stream()
				.map(v -> vesselMapper.toDto(v, nameHistoryRepository.findByVesselIdOrderByValidFromDesc(v.getId())))
				.toList();
	}

	@Transactional
	public VesselDto create(VesselRequestDto request) {
		VesselType vesselType = vesselTypeRepository.findById(request.getVesselTypeId())
				.orElseThrow(() -> new IllegalArgumentException("Vessel type not found: " + request.getVesselTypeId()));
		if (vesselRepository.findByImoNumber(request.getImoNumber()).isPresent()) {
			throw new IllegalArgumentException("Vessel with IMO number " + request.getImoNumber() + " already exists");
		}
		Vessel vessel = vesselMapper.toEntity(request, vesselType);
		vessel = vesselRepository.save(vessel);

		VesselNameHistory nameHistory = new VesselNameHistory();
		nameHistory.setVessel(vessel);
		nameHistory.setName(vessel.getName());
		nameHistory.setValidFrom(vessel.getCreatedAt());
		nameHistoryRepository.save(nameHistory);

		log.info("Created vessel: {} (IMO {})", vessel.getName(), vessel.getImoNumber());
		return vesselMapper.toDto(vessel, List.of(nameHistory));
	}

	@Transactional
	public VesselDto update(Long id, VesselUpdateDto request) {
		Vessel vessel = vesselRepository.findById(id).orElse(null);
		if (vessel == null) {
			return null;
		}
		vesselMapper.applyUpdate(vessel, request);
		vessel = vesselRepository.save(vessel);
		log.info("Updated vessel: {} (IMO {})", vessel.getName(), vessel.getImoNumber());
		return vesselMapper.toDto(vessel, nameHistoryRepository.findByVesselIdOrderByValidFromDesc(id));
	}

	@Transactional
	public VesselDto rename(Long id, String newName) {
		if (newName == null || newName.isBlank()) {
			throw new IllegalArgumentException("Name cannot be blank");
		}
		Vessel vessel = vesselRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Vessel not found: " + id));

		nameHistoryRepository.findByVesselIdAndValidToIsNull(id)
				.ifPresent(current -> {
					current.setValidTo(LocalDateTime.now());
					nameHistoryRepository.save(current);
				});

		VesselNameHistory newHistory = new VesselNameHistory();
		newHistory.setVessel(vessel);
		newHistory.setName(newName);
		newHistory.setValidFrom(LocalDateTime.now());
		nameHistoryRepository.save(newHistory);

		vessel.setName(newName);
		vessel = vesselRepository.save(vessel);
		log.info("Vessel {} renamed to '{}'", id, newName);
		return vesselMapper.toDto(vessel, nameHistoryRepository.findByVesselIdOrderByValidFromDesc(id));
	}

	@Transactional
	public VesselDto assignToCompany(Long vesselId, Long companyId) {
		Vessel vessel = vesselRepository.findById(vesselId)
				.orElseThrow(() -> new IllegalArgumentException("Vessel not found: " + vesselId));
		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

		companyHistoryRepository.findByVesselIdAndValidToIsNull(vesselId)
				.ifPresent(current -> {
					current.setValidTo(LocalDateTime.now());
					companyHistoryRepository.save(current);
				});

		VesselCompanyHistory newHistory = new VesselCompanyHistory();
		newHistory.setVessel(vessel);
		newHistory.setCompany(company);
		newHistory.setValidFrom(LocalDateTime.now());
		companyHistoryRepository.save(newHistory);

		vessel.setCurrentCompany(company);
		vessel = vesselRepository.save(vessel);
		log.info("Vessel {} assigned to company {}", vesselId, companyId);
		return vesselMapper.toDto(vessel, nameHistoryRepository.findByVesselIdOrderByValidFromDesc(vesselId));
	}

	@Transactional
	public VesselDto assignMaster(Long vesselId, Long userId) {
		Vessel vessel = vesselRepository.findById(vesselId)
				.orElseThrow(() -> new IllegalArgumentException("Vessel not found: " + vesselId));
		UserAccount user = userAccountRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

		boolean hasVesselRole = user.getUserRoles().stream()
				.map(UserRole::getRole)
				.anyMatch(role -> "ROLE_VESSEL".equals(role.getName()));
		if (!hasVesselRole) {
			throw new IllegalArgumentException("User does not have ROLE_VESSEL");
		}

		vessel.setMaster(user);
		vessel = vesselRepository.save(vessel);
		log.info("Vessel {} master set to user {}", vesselId, userId);
		return vesselMapper.toDto(vessel, nameHistoryRepository.findByVesselIdOrderByValidFromDesc(vesselId));
	}

	@Transactional
	public VesselDto regenerateAccessToken(Long vesselId) {
		Vessel vessel = vesselRepository.findById(vesselId)
				.orElseThrow(() -> new IllegalArgumentException("Vessel not found: " + vesselId));
		vessel.setAccessToken(UUID.randomUUID().toString());
		vessel = vesselRepository.save(vessel);
		log.info("Vessel {} access token regenerated", vesselId);
		return vesselMapper.toDto(vessel, nameHistoryRepository.findByVesselIdOrderByValidFromDesc(vesselId));
	}
}
