package uk.co.osiris.vrs.fleet;

import org.springframework.stereotype.Component;
import uk.co.osiris.vrs.company.Company;

@Component
public class FleetMapper {

    public FleetDto toDto(Fleet fleet) {
        if (fleet == null) return null;
        FleetDto dto = new FleetDto();
        dto.setId(fleet.getId());
        dto.setName(fleet.getName());
        dto.setDescription(fleet.getDescription());
        if (fleet.getCompany() != null) {
            dto.setCompanyId(fleet.getCompany().getId());
            dto.setCompanyName(fleet.getCompany().getName());
        }
        return dto;
    }

    public Fleet toEntity(FleetRequestDto request, Company company) {
        Fleet fleet = new Fleet();
        fleet.setCompany(company);
        fleet.setName(request.getName());
        fleet.setDescription(request.getDescription());
        return fleet;
    }

    public void applyUpdate(Fleet fleet, FleetRequestDto request) {
        fleet.setName(request.getName());
        fleet.setDescription(request.getDescription());
    }

    public FleetAssignmentDto toAssignmentDto(VesselFleetAssignment assignment) {
        if (assignment == null) return null;
        FleetAssignmentDto dto = new FleetAssignmentDto();
        dto.setId(assignment.getId());
        dto.setFleetId(assignment.getFleet().getId());
        dto.setValidFrom(assignment.getValidFrom());
        dto.setValidTo(assignment.getValidTo());
        if (assignment.getVessel() != null) {
            dto.setVesselId(assignment.getVessel().getId());
            dto.setVesselName(assignment.getVessel().getName());
            dto.setVesselImoNumber(assignment.getVessel().getImoNumber());
        }
        return dto;
    }
}
