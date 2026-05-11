package uk.co.osiris.vrs.report;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportMapper {

    public ReportFieldDefinitionDto toFieldDefinitionDto(ReportFieldDefinition def) {
        if (def == null) return null;
        ReportFieldDefinitionDto dto = new ReportFieldDefinitionDto();
        dto.setId(def.getId());
        dto.setName(def.getName());
        dto.setLabel(def.getLabel());
        dto.setMandatory(def.isMandatory());
        dto.setDisplayOrder(def.getDisplayOrder());
        dto.setMetaDataElementId(def.getMetaDataElementId());
        if (def.getReportType() != null) dto.setReportTypeId(def.getReportType().getId());
        if (def.getFieldType() != null) {
            dto.setFieldTypeId(def.getFieldType().getId());
            dto.setFieldTypeName(def.getFieldType().getName());
        }
        return dto;
    }

    public ReportDto toDto(Report report, List<ReportValue> values) {
        if (report == null) return null;
        ReportDto dto = new ReportDto();
        dto.setId(report.getId());
        dto.setSubmittedAt(report.getSubmittedAt());
        dto.setStatus(report.getStatus());
        if (report.getVessel() != null) {
            dto.setVesselId(report.getVessel().getId());
            dto.setVesselName(report.getVessel().getName());
        }
        if (report.getReportType() != null) {
            dto.setReportTypeId(report.getReportType().getId());
            dto.setReportTypeName(report.getReportType().getName());
        }
        if (report.getSubmittedBy() != null) {
            dto.setSubmittedById(report.getSubmittedBy().getId());
            dto.setSubmittedByEmail(report.getSubmittedBy().getEmail());
        }
        dto.setValues(values.stream().map(this::toValueDto).toList());
        return dto;
    }

    private ReportDto.ReportValueDto toValueDto(ReportValue rv) {
        ReportDto.ReportValueDto dto = new ReportDto.ReportValueDto();
        dto.setId(rv.getId());
        dto.setValue(rv.getValue());
        if (rv.getFieldDefinition() != null) {
            dto.setFieldDefinitionId(rv.getFieldDefinition().getId());
            dto.setFieldName(rv.getFieldDefinition().getName());
        }
        if (rv.getCustomField() != null) {
            dto.setCustomFieldId(rv.getCustomField().getId());
            dto.setCustomFieldName(rv.getCustomField().getName());
        }
        return dto;
    }
}
