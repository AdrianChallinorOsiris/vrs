package uk.co.osiris.vrs.vessel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VesselDto {

	private Long id;
	private String name;
	private String imoNumber;
	private Long vesselTypeId;
	private String vesselTypeName;
	private boolean active;
	private String accessToken;
	private Long currentCompanyId;
	private String currentCompanyName;
	private Long masterId;
	private String masterEmail;
	private List<VesselNameHistoryDto> nameHistory;
}
