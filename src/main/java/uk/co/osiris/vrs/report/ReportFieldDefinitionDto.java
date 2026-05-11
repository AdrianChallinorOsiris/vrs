package uk.co.osiris.vrs.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportFieldDefinitionDto {

    private Long id;
    private Long reportTypeId;
    private Long metaDataElementId;
    private String name;
    private String label;
    private Long fieldTypeId;
    private String fieldTypeName;
    private boolean mandatory;
    private int displayOrder;
}
