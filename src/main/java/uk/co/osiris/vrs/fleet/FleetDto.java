package uk.co.osiris.vrs.fleet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FleetDto {

    private Long id;
    private Long companyId;
    private String companyName;
    private String name;
    private String description;
}
