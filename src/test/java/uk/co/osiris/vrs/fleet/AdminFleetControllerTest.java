package uk.co.osiris.vrs.fleet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminFleetController using standalone MockMvc.
 * @PreAuthorize security is not enforced in standalone mode — verified by integration tests.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminFleetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FleetService fleetService;

    private FleetDto fleetDto;
    private FleetAssignmentDto assignmentDto;

    @BeforeEach
    void setUp() {
        AdminFleetController controller = new AdminFleetController(fleetService);
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

        when(fleetService.create(any())).thenReturn(fleetDto);
        when(fleetService.update(eq(1L), any())).thenReturn(fleetDto);
        doNothing().when(fleetService).delete(1L);
        when(fleetService.assignVessel(eq(1L), eq(2L))).thenReturn(assignmentDto);
        doNothing().when(fleetService).removeVessel(1L, 2L);
    }

    // --- POST /admin/v1/fleets ---

    @Test
    void create_returnsCreatedWithBody() throws Exception {
        mockMvc.perform(post("/admin/v1/fleets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFleetRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("North Sea Fleet"))
                .andExpect(jsonPath("$.companyId").value(1));
    }

    @Test
    void create_returnsBadRequest_onIllegalArgument() throws Exception {
        when(fleetService.create(any())).thenThrow(new IllegalArgumentException("Company not found: 99"));

        mockMvc.perform(post("/admin/v1/fleets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFleetRequest()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_returnsBadRequest_whenNameMissing() throws Exception {
        mockMvc.perform(post("/admin/v1/fleets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"companyId\": 1}"))
                .andExpect(status().isBadRequest());
    }

    // --- PUT /admin/v1/fleets/{id} ---

    @Test
    void update_returnsOkWithBody() throws Exception {
        mockMvc.perform(put("/admin/v1/fleets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFleetRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("North Sea Fleet"));
    }

    @Test
    void update_returnsBadRequest_onIllegalArgument() throws Exception {
        when(fleetService.update(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Fleet not found: 1"));

        mockMvc.perform(put("/admin/v1/fleets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFleetRequest()))
                .andExpect(status().isBadRequest());
    }

    // --- DELETE /admin/v1/fleets/{id} ---

    @Test
    void delete_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/admin/v1/fleets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_returnsNotFound_onIllegalArgument() throws Exception {
        doThrow(new IllegalArgumentException("Fleet not found: 999"))
                .when(fleetService).delete(999L);

        mockMvc.perform(delete("/admin/v1/fleets/999"))
                .andExpect(status().isNotFound());
    }

    // --- POST /admin/v1/fleets/{fleetId}/vessels ---

    @Test
    void assignVessel_returnsCreatedWithAssignment() throws Exception {
        mockMvc.perform(post("/admin/v1/fleets/1/vessels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vesselId\": 2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vesselId").value(2))
                .andExpect(jsonPath("$.fleetId").value(1))
                .andExpect(jsonPath("$.vesselImoNumber").value("9074729"));
    }

    @Test
    void assignVessel_returnsBadRequest_onIllegalArgument() throws Exception {
        when(fleetService.assignVessel(eq(1L), eq(2L)))
                .thenThrow(new IllegalArgumentException("Vessel is already assigned to this fleet"));

        mockMvc.perform(post("/admin/v1/fleets/1/vessels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vesselId\": 2}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void assignVessel_returnsBadRequest_whenVesselIdMissing() throws Exception {
        mockMvc.perform(post("/admin/v1/fleets/1/vessels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // --- DELETE /admin/v1/fleets/{fleetId}/vessels/{vesselId} ---

    @Test
    void removeVessel_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/admin/v1/fleets/1/vessels/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeVessel_returnsBadRequest_onIllegalArgument() throws Exception {
        doThrow(new IllegalArgumentException("Vessel is not currently assigned to this fleet"))
                .when(fleetService).removeVessel(1L, 99L);

        mockMvc.perform(delete("/admin/v1/fleets/1/vessels/99"))
                .andExpect(status().isBadRequest());
    }

    private static String validFleetRequest() {
        return """
                {
                    "companyId": 1,
                    "name": "North Sea Fleet",
                    "description": "Vessels operating in the North Sea"
                }
                """;
    }
}
