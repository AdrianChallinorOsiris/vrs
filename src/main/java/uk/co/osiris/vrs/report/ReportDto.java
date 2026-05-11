package uk.co.osiris.vrs.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReportDto {

    private Long id;
    private Long vesselId;
    private String vesselName;
    private Long reportTypeId;
    private String reportTypeName;
    private LocalDateTime submittedAt;
    private Long submittedById;
    private String submittedByEmail;
    private String status;
    private List<ReportValueDto> values;

    @Getter
    @Setter
    public static class ReportValueDto {
        private Long id;
        private Long fieldDefinitionId;
        private String fieldName;
        private Long customFieldId;
        private String customFieldName;
        private String value;
    }
}
