package uk.co.osiris.vrs.fleet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserFleetController using standalone MockMvc.
 * @PreAuthorize security is not enforced in standalone mode — verified by integration tests.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserFleetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FleetService fleetService;

    private FleetDto fleetDto;
    private FleetAssignmentDto assignmentDto;

    @BeforeEach
    void setUp() {
        UserFleetController controller = new UserFleetController(fleetService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        fleetDto = new FleetDto();
        fleetDto.setId(1L);
        fleetDto.setName("North Sea Fleet");
        fleetDto.setDescription("Vessels operating in the North Sea");
        fleetDto.setCompanyId(1L);
        fleetDto.setCompanyName("ACME Corp");

        assignmentDto = new FleetAssignmentDto();
        assignmentDto.setId(10L);
        assignmentDto.setFleetId(1L);
        assignmentDto.setVesselId(2L);
        assignmentDto.setVesselName("Test Vessel");
        assignmentDto.setVesselImoNumber("9074729");
        assignmentDto.setValidFrom(LocalDateTime.now());

        when(fleetService.findAll()).thenReturn(List.of(fleetDto));
        when(fleetService.findByCompany(1L)).thenReturn(List.of(fleetDto));
        when(fleetService.findById(1L)).thenReturn(fleetDto);
        when(fleetService.findById(999L)).thenReturn(null);
        when(fleetService.findCurrentVessels(1L)).thenReturn(List.of(assignmentDto));
    }

    // --- GET /user/v1/fleets ---

    @Test
    void list_returnsAllFleets_whenNoCompanyFilter() throws Exception {
        mockMvc.perform(get("/user/v1/fleets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("North Sea Fleet"))
                .andExpect(jsonPath("$[0].companyId").value(1));
    }

    @Test
    void list_returnsFleetsByCompany_whenCompanyParamGiven() throws Exception {
        mockMvc.perform(get("/user/v1/fleets").param("companyId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyId").value(1));
    }

    @Test
    void list_returnsEmptyList_whenCompanyHasNoFleets() throws Exception {
        when(fleetService.findByCompany(99L)).thenReturn(List.of());

        mockMvc.perform(get("/user/v1/fleets").param("companyId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // --- GET /user/v1/fleets/{id} ---

    @Test
    void getById_returnsFleet_whenFound() throws Exception {
        mockMvc.perform(get("/user/v1/fleets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("North Sea Fleet"))
                .andExpect(jsonPath("$.companyName").value("ACME Corp"));
    }

    @Test
    void getById_returnsNotFound_whenFleetMissing() throws Exception {
        mockMvc.perform(get("/user/v1/fleets/999"))
                .andExpect(status().isNotFound());
    }

    // --- GET /user/v1/fleets/{id}/vessels ---

    @Test
    void getVessels_returnsCurrentAssignments() throws Exception {
        mockMvc.perform(get("/user/v1/fleets/1/vessels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vesselId").value(2))
                .andExpect(jsonPath("$[0].vesselName").value("Test Vessel"))
                .andExpect(jsonPath("$[0].vesselImoNumber").value("9074729"))
                .andExpect(jsonPath("$[0].fleetId").value(1));
    }

    @Test
    void getVessels_returnsEmptyList_whenFleetHasNoVessels() throws Exception {
        when(fleetService.findCurrentVessels(1L)).thenReturn(List.of());

        mockMvc.perform(get("/user/v1/fleets/1/vessels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
