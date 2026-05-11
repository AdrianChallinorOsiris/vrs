package uk.co.osiris.vrs.vessel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VesselNameHistoryDto {

    private Long id;
    private String name;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}
