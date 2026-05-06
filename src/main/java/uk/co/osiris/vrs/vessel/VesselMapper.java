package uk.co.osiris.vrs.vessel;

import org.springframework.stereotype.Component;

@Component
public class VesselMapper {

	public VesselDto toDto(Vessel vessel) {
		if (vessel == null) {
			return null;
		}
		VesselDto dto = new VesselDto();
		dto.setId(vessel.getId());
		dto.setName(vessel.getName());
		dto.setImoNumber(vessel.getImoNumber());
		dto.setActive(vessel.isActive());
		if (vessel.getVesselType() != null) {
			dto.setVesselTypeId(vessel.getVesselType().getId());
			dto.setVesselTypeName(vessel.getVesselType().getName());
		}
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
