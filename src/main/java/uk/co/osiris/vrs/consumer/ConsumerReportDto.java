package uk.co.osiris.vrs.consumer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConsumerReportDto {

    private Long id;
    private Long fleetId;
    private String fleetName;
    private String name;
    private String description;
    private List<ConsumerReportFieldDto> fields;

    @Getter
    @Setter
    public static class ConsumerReportFieldDto {
        private Long id;
        private Long fieldDefinitionId;
        private String fieldDefinitionName;
        private Long customFieldId;
        private String customFieldName;
        private String displayName;
        private int displayOrder;
        private String formatHint;
    }
}
