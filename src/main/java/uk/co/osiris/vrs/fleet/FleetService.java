package uk.co.osiris.vrs.fleet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.osiris.vrs.company.Company;
import uk.co.osiris.vrs.company.CompanyRepository;
import uk.co.osiris.vrs.vessel.Vessel;
import uk.co.osiris.vrs.vessel.VesselRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FleetService {

    private final FleetRepository fleetRepository;
    private final VesselFleetAssignmentRepository assignmentRepository;
    private final FleetMapper fleetMapper;
    private final CompanyRepository companyRepository;
    private final VesselRepository vesselRepository;

    @Transactional(readOnly = true)
    public List<FleetDto> findAll() {
        return fleetRepository.findAll().stream().map(fleetMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public FleetDto findById(Long id) {
        return fleetRepository.findById(id).map(fleetMapper::toDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<FleetDto> findByCompany(Long companyId) {
        return fleetRepository.findByCompanyId(companyId).stream().map(fleetMapper::toDto).toList();
    }

    @Transactional
    public FleetDto create(FleetRequestDto request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + request.getCompanyId()));
        Fleet fleet = fleetMapper.toEntity(request, company);
        fleet = fleetRepository.save(fleet);
        log.info("Created fleet '{}' for company {}", fleet.getName(), company.getId());
        return fleetMapper.toDto(fleet);
    }

    @Transactional
    public FleetDto update(Long id, FleetRequestDto request) {
        Fleet fleet = fleetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fleet not found: " + id));
        fleetMapper.applyUpdate(fleet, request);
        fleet = fleetRepository.save(fleet);
        log.info("Updated fleet {} - '{}'", id, fleet.getName());
        return fleetMapper.toDto(fleet);
    }

    @Transactional
    public void delete(Long id) {
        if (!fleetRepository.existsById(id)) {
            throw new IllegalArgumentException("Fleet not found: " + id);
        }
        fleetRepository.deleteById(id);
        log.info("Deleted fleet {}", id);
    }

    @Transactional(readOnly = true)
    public List<FleetAssignmentDto> findCurrentVessels(Long fleetId) {
        return assignmentRepository.findCurrentByFleetId(fleetId).stream()
                .map(fleetMapper::toAssignmentDto)
                .toList();
    }

    @Transactional
    public FleetAssignmentDto assignVessel(Long fleetId, Long vesselId) {
        Fleet fleet = fleetRepository.findById(fleetId)
                .orElseThrow(() -> new IllegalArgumentException("Fleet not found: " + fleetId));
        Vessel vessel = vesselRepository.findById(vesselId)
                .orElseThrow(() -> new IllegalArgumentException("Vessel not found: " + vesselId));
        if (assignmentRepository.findByFleetIdAndVesselIdAndValidToIsNull(fleetId, vesselId).isPresent()) {
            throw new IllegalArgumentException("Vessel is already assigned to this fleet");
        }
        VesselFleetAssignment assignment = new VesselFleetAssignment();
        assignment.setFleet(fleet);
        assignment.setVessel(vessel);
        assignment.setValidFrom(LocalDateTime.now());
        assignment = assignmentRepository.save(assignment);
        log.info("Vessel {} assigned to fleet {}", vesselId, fleetId);
        return fleetMapper.toAssignmentDto(assignment);
    }

    @Transactional
    public void removeVessel(Long fleetId, Long vesselId) {
        VesselFleetAssignment assignment = assignmentRepository
                .findByFleetIdAndVesselIdAndValidToIsNull(fleetId, vesselId)
                .orElseThrow(() -> new IllegalArgumentException("Vessel is not currently assigned to this fleet"));
        assignment.setValidTo(LocalDateTime.now());
        assignmentRepository.save(assignment);
        log.info("Vessel {} removed from fleet {}", vesselId, fleetId);
    }
}
