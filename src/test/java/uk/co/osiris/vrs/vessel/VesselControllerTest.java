package uk.co.osiris.vrs.vessel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for VesselController.
 * Uses standalone MockMvc (no Spring context) to avoid JPA/Data dependencies.
 * Role-based security (@PreAuthorize) is enforced at runtime by Spring Security
 * and can be verified via integration tests when the full application context is available.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VesselControllerTest {

	private MockMvc mockMvc;

	@Mock
	private VesselService vesselService;

	private VesselDto vesselDto;

	@BeforeEach
	void setUp() {
		VesselController controller = new VesselController(vesselService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		vesselDto = new VesselDto();
		vesselDto.setId(1L);
		vesselDto.setName("Test Vessel");
		vesselDto.setImoNumber("9074729");
		vesselDto.setVesselTypeId(1L);
		vesselDto.setVesselTypeName("Tanker");
		vesselDto.setActive(true);

		when(vesselService.findById(1L)).thenReturn(vesselDto);
		when(vesselService.findAll()).thenReturn(List.of(vesselDto));
		when(vesselService.create(any())).thenReturn(vesselDto);
		when(vesselService.update(eq(1L), any())).thenReturn(vesselDto);
		when(vesselService.update(eq(999L), any())).thenReturn(null);
	}

	@Test
	void listReturnsVessels() throws Exception {
		mockMvc.perform(get("/vessels/v1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Test Vessel"))
				.andExpect(jsonPath("$[0].imoNumber").value("9074729"));
	}

	@Test
	void getByIdReturnsVessel() throws Exception {
		mockMvc.perform(get("/vessels/v1/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Test Vessel"))
				.andExpect(jsonPath("$.imoNumber").value("9074729"));
	}

	@Test
	void getByIdReturns404WhenNotFound() throws Exception {
		mockMvc.perform(get("/vessels/v1/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void createReturnsCreatedVessel() throws Exception {
		mockMvc.perform(post("/vessels/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(validCreateRequest()))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Test Vessel"));
	}

	@Test
	void createReturns400ForInvalidImo() throws Exception {
		when(vesselService.create(any())).thenThrow(new IllegalArgumentException("Invalid IMO number"));

		mockMvc.perform(post("/vessels/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(validCreateRequest()))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateReturnsUpdatedVessel() throws Exception {
		mockMvc.perform(put("/vessels/v1/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Updated Name\",\"active\":false}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Test Vessel"));
	}

	@Test
	void updateReturns404WhenNotFound() throws Exception {
		mockMvc.perform(put("/vessels/v1/999")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Updated Name\"}"))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateReturns400ForBlankName() throws Exception {
		when(vesselService.update(eq(1L), any())).thenThrow(new IllegalArgumentException("Name cannot be blank"));

		mockMvc.perform(put("/vessels/v1/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"  \",\"active\":true}"))
				.andExpect(status().isBadRequest());
	}

	private static String validCreateRequest() {
		return """
				{
					"name": "Test Vessel",
					"imoNumber": "9074729",
					"vesselTypeId": 1,
					"active": true
				}
				""";
	}
}
