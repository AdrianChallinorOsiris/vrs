package uk.co.osiris.vrs.fleet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FleetRequestDto {

    @NotNull
    private Long companyId;

    @NotBlank
    private String name;

    private String description;
}
