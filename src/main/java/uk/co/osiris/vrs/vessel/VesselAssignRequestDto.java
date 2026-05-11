package uk.co.osiris.vrs.vessel;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VesselAssignRequestDto {

    @NotNull
    private Long id;
}
