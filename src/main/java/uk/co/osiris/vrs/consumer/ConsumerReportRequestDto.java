package uk.co.osiris.vrs.consumer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerReportRequestDto {

    @NotNull
    private Long fleetId;

    @NotBlank
    private String name;

    private String description;
}
