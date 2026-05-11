package uk.co.osiris.vrs.vessel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VesselRenameRequestDto {

    @NotBlank
    private String name;
}
