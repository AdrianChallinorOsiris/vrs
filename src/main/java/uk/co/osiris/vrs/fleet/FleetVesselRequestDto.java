package uk.co.osiris.vrs.fleet;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FleetVesselRequestDto {

    @NotNull
    private Long vesselId;
}
