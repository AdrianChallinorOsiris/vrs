package uk.co.osiris.vrs.consumer;

import org.springframework.stereotype.Component;
import uk.co.osiris.vrs.fleet.Fleet;

import java.util.List;

@Component
public class ConsumerReportMapper {

    public ConsumerReportDto toDto(ConsumerReport report, List<ConsumerReportField> fields) {
        if (report == null) return null;
        ConsumerReportDto dto = new ConsumerReportDto();
        dto.setId(report.getId());
        dto.setName(report.getName());
        dto.setDescription(report.getDescription());
        if (report.getFleet() != null) {
            dto.setFleetId(report.getFleet().getId());
            dto.setFleetName(report.getFleet().getName());
        }
        dto.setFields(fields.stream().map(this::toFieldDto).toList());
        return dto;
    }

    public ConsumerReportDto.ConsumerReportFieldDto toFieldDto(ConsumerReportField field) {
        ConsumerReportDto.ConsumerReportFieldDto dto = new ConsumerReportDto.ConsumerReportFieldDto();
        dto.setId(field.getId());
        dto.setDisplayName(field.getDisplayName());
        dto.setDisplayOrder(field.getDisplayOrder());
        dto.setFormatHint(field.getFormatHint());
        if (field.getFieldDefinition() != null) {
            dto.setFieldDefinitionId(field.getFieldDefinition().getId());
            dto.setFieldDefinitionName(field.getFieldDefinition().getName());
        }
        if (field.getCustomField() != null) {
            dto.setCustomFieldId(field.getCustomField().getId());
            dto.setCustomFieldName(field.getCustomField().getName());
        }
        return dto;
    }

    public ConsumerReport toEntity(ConsumerReportRequestDto request, Fleet fleet) {
        ConsumerReport report = new ConsumerReport();
        report.setFleet(fleet);
        report.setName(request.getName());
        report.setDescription(request.getDescription());
        return report;
    }
}
