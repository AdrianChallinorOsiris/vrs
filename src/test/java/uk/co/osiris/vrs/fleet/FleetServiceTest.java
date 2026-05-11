package uk.co.osiris.vrs.fleet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.osiris.vrs.company.Company;
import uk.co.osiris.vrs.company.CompanyRepository;
import uk.co.osiris.vrs.vessel.Vessel;
import uk.co.osiris.vrs.vessel.VesselRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FleetServiceTest {

    @Mock FleetRepository fleetRepository;
    @Mock VesselFleetAssignmentRepository assignmentRepository;
    @Mock FleetMapper fleetMapper;
    @Mock CompanyRepository companyRepository;
    @Mock VesselRepository vesselRepository;

    @InjectMocks FleetService fleetService;

    private Company company;
    private Fleet fleet;
    private FleetDto fleetDto;
    private FleetRequestDto requestDto;
    private Vessel vessel;
    private VesselFleetAssignment assignment;
    private FleetAssignmentDto assignmentDto;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("ACME Corp");
        company.setEmail("info@acme.com");

        fleet = new Fleet();
        fleet.setId(1L);
        fleet.setName("North Sea Fleet");
        fleet.setDescription("Vessels operating in the North Sea");
        fleet.setCompany(company);

        fleetDto = new FleetDto();
        fleetDto.setId(1L);
        fleetDto.setName("North Sea Fleet");
        fleetDto.setDescription("Vessels operating in the North Sea");
        fleetDto.setCompanyId(1L);
        fleetDto.setCompanyName("ACME Corp");

        requestDto = new FleetRequestDto();
        requestDto.setCompanyId(1L);
        requestDto.setName("North Sea Fleet");
        requestDto.setDescription("Vessels operating in the North Sea");

        vessel = new Vessel();
        vessel.setId(2L);
        vessel.setName("Test Vessel");
        vessel.setImoNumber("9074729");

        assignment = new VesselFleetAssignment();
        assignment.setId(10L);
        assignment.setFleet(fleet);
        assignment.setVessel(vessel);
        assignment.setValidFrom(LocalDateTime.now());

        assignmentDto = new FleetAssignmentDto();
        assignmentDto.setId(10L);
        assignmentDto.setFleetId(1L);
        assignmentDto.setVesselId(2L);
        assignmentDto.setVesselName("Test Vessel");
        assignmentDto.setVesselImoNumber("9074729");
    }

    // --- findAll ---

    @Test
    void findAll_returnsMappedDtos() {
        when(fleetRepository.findAll()).thenReturn(List.of(fleet));
        when(fleetMapper.toDto(fleet)).thenReturn(fleetDto);

        List<FleetDto> result = fleetService.findAll();

        assertEquals(1, result.size());
        assertEquals("North Sea Fleet", result.get(0).getName());
    }

    @Test
    void findAll_returnsEmptyList_whenNoFleets() {
        when(fleetRepository.findAll()).thenReturn(List.of());

        List<FleetDto> result = fleetService.findAll();

        assertTrue(result.isEmpty());
    }

    // --- findById ---

    @Test
    void findById_returnsDto_whenFound() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleet));
        when(fleetMapper.toDto(fleet)).thenReturn(fleetDto);

        FleetDto result = fleetService.findById(1L);

        assertNotNull(result);
        assertEquals("North Sea Fleet", result.getName());
        assertEquals(1L, result.getCompanyId());
    }

    @Test
    void findById_returnsNull_whenNotFound() {
        when(fleetRepository.findById(999L)).thenReturn(Optional.empty());

        FleetDto result = fleetService.findById(999L);

        assertNull(result);
    }

    // --- findByCompany ---

    @Test
    void findByCompany_returnsDtos_forCompany() {
        when(fleetRepository.findByCompanyId(1L)).thenReturn(List.of(fleet));
        when(fleetMapper.toDto(fleet)).thenReturn(fleetDto);

        List<FleetDto> result = fleetService.findByCompany(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCompanyId());
    }

    // --- create ---

    @Test
    void create_throwsWhenCompanyNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> fleetService.create(requestDto));
        verify(fleetRepository, never()).save(any());
    }

    @Test
    void create_savesAndReturnsDto() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(fleetMapper.toEntity(requestDto, company)).thenReturn(fleet);
        when(fleetRepository.save(fleet)).thenReturn(fleet);
        when(fleetMapper.toDto(fleet)).thenReturn(fleetDto);

        FleetDto result = fleetService.create(requestDto);

        assertNotNull(result);
        assertEquals("North Sea Fleet", result.getName());
        verify(fleetRepository).save(fleet);
    }

    // --- update ---

    @Test
    void update_throwsWhenFleetNotFound() {
        when(fleetRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> fleetService.update(999L, requestDto));
        verify(fleetRepository, never()).save(any());
    }

    @Test
    void update_appliesChangesAndReturnsDto() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleet));
        when(fleetRepository.save(fleet)).thenReturn(fleet);
        when(fleetMapper.toDto(fleet)).thenReturn(fleetDto);

        FleetDto result = fleetService.update(1L, requestDto);

        assertNotNull(result);
        verify(fleetMapper).applyUpdate(fleet, requestDto);
        verify(fleetRepository).save(fleet);
    }

    // --- delete ---

    @Test
    void delete_throwsWhenFleetNotFound() {
        when(fleetRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> fleetService.delete(999L));
        verify(fleetRepository, never()).deleteById(any());
    }

    @Test
    void delete_callsDeleteById_whenFound() {
        when(fleetRepository.existsById(1L)).thenReturn(true);

        fleetService.delete(1L);

        verify(fleetRepository).deleteById(1L);
    }

    // --- findCurrentVessels ---

    @Test
    void findCurrentVessels_returnsMappedAssignmentDtos() {
        when(assignmentRepository.findCurrentByFleetId(1L)).thenReturn(List.of(assignment));
        when(fleetMapper.toAssignmentDto(assignment)).thenReturn(assignmentDto);

        List<FleetAssignmentDto> result = fleetService.findCurrentVessels(1L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getVesselId());
        assertEquals("9074729", result.get(0).getVesselImoNumber());
    }

    // --- assignVessel ---

    @Test
    void assignVessel_throwsWhenFleetNotFound() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> fleetService.assignVessel(1L, 2L));
    }

    @Test
    void assignVessel_throwsWhenVesselNotFound() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleet));
        when(vesselRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> fleetService.assignVessel(1L, 2L));
    }

    @Test
    void assignVessel_throwsWhenVesselAlreadyAssigned() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleet));
        when(vesselRepository.findById(2L)).thenReturn(Optional.of(vessel));
        when(assignmentRepository.findByFleetIdAndVesselIdAndValidToIsNull(1L, 2L))
                .thenReturn(Optional.of(assignment));

        assertThrows(IllegalArgumentException.class, () -> fleetService.assignVessel(1L, 2L));
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void assignVessel_savesAndReturnsDto() {
        when(fleetRepository.findById(1L)).thenReturn(Optional.of(fleet));
        when(vesselRepository.findById(2L)).thenReturn(Optional.of(vessel));
        when(assignmentRepository.findByFleetIdAndVesselIdAndValidToIsNull(1L, 2L))
                .thenReturn(Optional.empty());
        when(assignmentRepository.save(any())).thenReturn(assignment);
        when(fleetMapper.toAssignmentDto(assignment)).thenReturn(assignmentDto);

        FleetAssignmentDto result = fleetService.assignVessel(1L, 2L);

        assertNotNull(result);
        assertEquals(2L, result.getVesselId());
        verify(assignmentRepository).save(any(VesselFleetAssignment.class));
    }

    // --- removeVessel ---

    @Test
    void removeVessel_throwsWhenVesselNotCurrentlyAssigned() {
        when(assignmentRepository.findByFleetIdAndVesselIdAndValidToIsNull(1L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> fleetService.removeVessel(1L, 2L));
    }

    @Test
    void removeVessel_setsValidToAndSaves() {
        when(assignmentRepository.findByFleetIdAndVesselIdAndValidToIsNull(1L, 2L))
                .thenReturn(Optional.of(assignment));

        fleetService.removeVessel(1L, 2L);

        assertNotNull(assignment.getValidTo());
        verify(assignmentRepository).save(assignment);
    }
}
