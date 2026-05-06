package uk.co.osiris.vrs.vessel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VesselServiceTest {

	@Mock
	VesselRepository vesselRepository;

	@Mock
	VesselTypeRepository vesselTypeRepository;

	@Mock
	VesselMapper vesselMapper;

	@InjectMocks
	VesselService vesselService;

	private Vessel vessel;
	private VesselType vesselType;
	private VesselDto vesselDto;
	private VesselRequestDto requestDto;
	private VesselUpdateDto updateDto;

	@BeforeEach
	void setUp() {
		vesselType = new VesselType();
		vesselType.setId(1L);
		vesselType.setName("Tanker");

		vessel = new Vessel();
		vessel.setId(1L);
		vessel.setName("Test Vessel");
		vessel.setImoNumber("9074729");
		vessel.setVesselType(vesselType);
		vessel.setActive(true);

		vesselDto = new VesselDto();
		vesselDto.setId(1L);
		vesselDto.setName("Test Vessel");
		vesselDto.setImoNumber("9074729");
		vesselDto.setVesselTypeId(1L);
		vesselDto.setVesselTypeName("Tanker");
		vesselDto.setActive(true);

		requestDto = new VesselRequestDto();
		requestDto.setName("Test Vessel");
		requestDto.setImoNumber("9074729");
		requestDto.setVesselTypeId(1L);
		requestDto.setActive(true);

		updateDto = new VesselUpdateDto();
		updateDto.setName("Updated Name");
		updateDto.setActive(false);
	}

	@Test
	void findByIdReturnsVesselWhenFound() {
		when(vesselRepository.findById(1L)).thenReturn(Optional.of(vessel));
		when(vesselMapper.toDto(vessel)).thenReturn(vesselDto);

		VesselDto result = vesselService.findById(1L);

		assertNotNull(result);
		assertEquals("Test Vessel", result.getName());
		assertEquals("9074729", result.getImoNumber());
	}

	@Test
	void findByIdReturnsNullWhenNotFound() {
		when(vesselRepository.findById(999L)).thenReturn(Optional.empty());

		VesselDto result = vesselService.findById(999L);

		assertNull(result);
	}

	@Test
	void findAllReturnsAllVessels() {
		when(vesselRepository.findAll()).thenReturn(List.of(vessel));
		when(vesselMapper.toDto(vessel)).thenReturn(vesselDto);

		List<VesselDto> result = vesselService.findAll();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Test Vessel", result.get(0).getName());
	}

	@Test
	void createThrowsWhenVesselTypeNotFound() {
		when(vesselTypeRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> vesselService.create(requestDto));
	}

	@Test
	void createThrowsWhenImoAlreadyExists() {
		when(vesselTypeRepository.findById(1L)).thenReturn(Optional.of(vesselType));
		when(vesselRepository.findByImoNumber("9074729")).thenReturn(Optional.of(vessel));

		assertThrows(IllegalArgumentException.class, () -> vesselService.create(requestDto));
	}

	@Test
	void createSavesAndReturnsVessel() {
		when(vesselTypeRepository.findById(1L)).thenReturn(Optional.of(vesselType));
		when(vesselRepository.findByImoNumber("9074729")).thenReturn(Optional.empty());
		when(vesselMapper.toEntity(any(), any())).thenReturn(vessel);
		when(vesselRepository.save(vessel)).thenReturn(vessel);
		when(vesselMapper.toDto(vessel)).thenReturn(vesselDto);

		VesselDto result = vesselService.create(requestDto);

		assertNotNull(result);
		assertEquals("Test Vessel", result.getName());
		verify(vesselRepository).save(vessel);
	}

	@Test
	void updateReturnsNullWhenVesselNotFound() {
		when(vesselRepository.findById(999L)).thenReturn(Optional.empty());

		VesselDto result = vesselService.update(999L, updateDto);

		assertNull(result);
	}

	@Test
	void updateAppliesChangesAndSaves() {
		when(vesselRepository.findById(1L)).thenReturn(Optional.of(vessel));
		when(vesselRepository.save(vessel)).thenReturn(vessel);
		when(vesselMapper.toDto(vessel)).thenReturn(vesselDto);

		VesselDto result = vesselService.update(1L, updateDto);

		assertNotNull(result);
		verify(vesselMapper).applyUpdate(vessel, updateDto);
		verify(vesselRepository).save(vessel);
	}
}
