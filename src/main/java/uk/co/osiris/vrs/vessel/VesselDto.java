package uk.co.osiris.vrs.vessel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VesselDto {

	private Long id;
	private String name;
	private String imoNumber;
	private Long vesselTypeId;
	private String vesselTypeName;
	private boolean active;
}
