package uk.co.osiris.vrs.vessel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VesselService {

	private final VesselRepository vesselRepository;
	private final VesselTypeRepository vesselTypeRepository;
	private final VesselMapper vesselMapper;

	@Transactional(readOnly = true)
	public VesselDto findById(Long id) {
		return vesselRepository.findById(id)
				.map(vesselMapper::toDto)
				.orElse(null);
	}

	@Transactional(readOnly = true)
	public List<VesselDto> findAll() {
		return vesselRepository.findAll().stream()
				.map(vesselMapper::toDto)
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
		log.info("Created vessel: {} (IMO {})", vessel.getName(), vessel.getImoNumber());
		return vesselMapper.toDto(vessel);
	}

	@Transactional
	public VesselDto update(Long id, VesselUpdateDto request) {
		Vessel vessel = vesselRepository.findById(id)
				.orElse(null);
		if (vessel == null) {
			return null;
		}
		vesselMapper.applyUpdate(vessel, request);
		vessel = vesselRepository.save(vessel);
		log.info("Updated vessel: {} (IMO {})", vessel.getName(), vessel.getImoNumber());
		return vesselMapper.toDto(vessel);
	}
}
