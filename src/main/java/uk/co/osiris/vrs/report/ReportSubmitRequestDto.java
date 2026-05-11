package uk.co.osiris.vrs.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportSubmitRequestDto {

    @NotNull
    private Long reportTypeId;

    @NotNull
    private List<ReportValueRequestDto> values;

    @Getter
    @Setter
    public static class ReportValueRequestDto {

        private Long fieldDefinitionId;
        private Long customFieldId;

        @NotBlank
        private String value;
    }
}
