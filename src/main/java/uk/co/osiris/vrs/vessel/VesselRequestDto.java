package uk.co.osiris.vrs.vessel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VesselRequestDto {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "IMO number is required")
	@Size(min = 7, max = 7, message = "IMO number must be exactly 7 digits")
	@Pattern(regexp = "[1-9][0-9]{6}", message = "IMO number must be 7 digits and cannot start with zero")
	private String imoNumber;

	@NotNull(message = "Vessel type is required")
	private Long vesselTypeId;

	private boolean active = true;
}
