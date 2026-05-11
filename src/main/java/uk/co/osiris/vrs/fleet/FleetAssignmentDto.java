package uk.co.osiris.vrs.fleet;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FleetAssignmentDto {

    private Long id;
    private Long vesselId;
    private String vesselName;
    private String vesselImoNumber;
    private Long fleetId;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}
