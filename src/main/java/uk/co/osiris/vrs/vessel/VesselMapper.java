package uk.co.osiris.vrs.vessel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class VesselMapper {

	public VesselDto toDto(Vessel vessel) {
		return toDto(vessel, List.of());
	}

	public VesselDto toDto(Vessel vessel, List<VesselNameHistory> nameHistory) {
		if (vessel == null) {
			return null;
		}
		VesselDto dto = new VesselDto();
		dto.setId(vessel.getId());
		dto.setName(vessel.getName());
		dto.setImoNumber(vessel.getImoNumber());
		dto.setActive(vessel.isActive());
		dto.setAccessToken(vessel.getAccessToken());
		if (vessel.getVesselType() != null) {
			dto.setVesselTypeId(vessel.getVesselType().getId());
			dto.setVesselTypeName(vessel.getVesselType().getName());
		}
		if (vessel.getCurrentCompany() != null) {
			dto.setCurrentCompanyId(vessel.getCurrentCompany().getId());
			dto.setCurrentCompanyName(vessel.getCurrentCompany().getName());
		}
		if (vessel.getMaster() != null) {
			dto.setMasterId(vessel.getMaster().getId());
			dto.setMasterEmail(vessel.getMaster().getEmail());
		}
		dto.setNameHistory(nameHistory.stream().map(this::toNameHistoryDto).toList());
		return dto;
	}

	public VesselNameHistoryDto toNameHistoryDto(VesselNameHistory history) {
		VesselNameHistoryDto dto = new VesselNameHistoryDto();
		dto.setId(history.getId());
		dto.setName(history.getName());
		dto.setValidFrom(history.getValidFrom());
		dto.setValidTo(history.getValidTo());
		return dto;
	}

	public Vessel toEntity(VesselRequestDto dto, VesselType vesselType) {
		if (dto == null) {
			return null;
		}
		if (!ImoNumberValidator.isValid(dto.getImoNumber())) {
			throw new IllegalArgumentException("Invalid IMO number: check digit verification failed");
		}
		Vessel vessel = new Vessel();
		vessel.setName(dto.getName());
		vessel.setImoNumber(dto.getImoNumber());
		vessel.setVesselType(vesselType);
		vessel.setActive(dto.isActive());
		vessel.setAccessToken(UUID.randomUUID().toString());
		return vessel;
	}

	public void applyUpdate(Vessel vessel, VesselUpdateDto dto) {
		if (vessel == null || dto == null) {
			return;
		}
		if (dto.getName() != null) {
			if (dto.getName().isBlank()) {
				throw new IllegalArgumentException("Name cannot be blank");
			}
			vessel.setName(dto.getName());
		}
		if (dto.getActive() != null) {
			vessel.setActive(dto.getActive());
		}
	}
}
