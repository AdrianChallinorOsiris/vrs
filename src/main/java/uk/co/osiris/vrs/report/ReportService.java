package uk.co.osiris.vrs.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.osiris.vrs.users_roles.UserAccount;
import uk.co.osiris.vrs.users_roles.UserAccountRepository;
import uk.co.osiris.vrs.vessel.Vessel;
import uk.co.osiris.vrs.vessel.VesselRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportValueRepository reportValueRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportFieldDefinitionRepository fieldDefinitionRepository;
    private final FleetCustomFieldRepository customFieldRepository;
    private final VesselRepository vesselRepository;
    private final UserAccountRepository userAccountRepository;
    private final ReportMapper reportMapper;

    @Transactional(readOnly = true)
    public List<ReportFieldDefinitionDto> getFieldDefinitions(Long reportTypeId) {
        return fieldDefinitionRepository.findByReportTypeIdOrderByDisplayOrder(reportTypeId).stream()
                .map(reportMapper::toFieldDefinitionDto)
                .toList();
    }

    @Transactional
    public ReportDto submit(Long vesselId, ReportSubmitRequestDto request, String submitterEmail) {
        Vessel vessel = vesselRepository.findById(vesselId)
                .orElseThrow(() -> new IllegalArgumentException("Vessel not found: " + vesselId));
        ReportType reportType = reportTypeRepository.findById(request.getReportTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Report type not found: " + request.getReportTypeId()));
        if (!reportType.isActive()) {
            throw new IllegalArgumentException("Report type is not active: " + reportType.getName());
        }

        List<ReportFieldDefinition> mandatoryFields =
                fieldDefinitionRepository.findByReportTypeIdOrderByDisplayOrder(reportType.getId())
                        .stream().filter(ReportFieldDefinition::isMandatory).toList();
        List<Long> providedFieldIds = request.getValues().stream()
                .filter(v -> v.getFieldDefinitionId() != null)
                .map(ReportSubmitRequestDto.ReportValueRequestDto::getFieldDefinitionId)
                .toList();
        for (ReportFieldDefinition mandatory : mandatoryFields) {
            if (!providedFieldIds.contains(mandatory.getId())) {
                throw new IllegalArgumentException("Missing mandatory field: " + mandatory.getName());
            }
        }

        UserAccount submitter = submitterEmail != null
                ? userAccountRepository.findByEmail(submitterEmail)
                : null;

        Report report = new Report();
        report.setVessel(vessel);
        report.setReportType(reportType);
        report.setSubmittedBy(submitter);
        report = reportRepository.save(report);

        List<ReportValue> savedValues = new ArrayList<>();
        for (ReportSubmitRequestDto.ReportValueRequestDto valueRequest : request.getValues()) {
            ReportValue rv = new ReportValue();
            rv.setReport(report);
            rv.setValue(valueRequest.getValue());
            if (valueRequest.getFieldDefinitionId() != null) {
                rv.setFieldDefinition(fieldDefinitionRepository.findById(valueRequest.getFieldDefinitionId())
                        .orElseThrow(() -> new IllegalArgumentException("Field definition not found: " + valueRequest.getFieldDefinitionId())));
            } else if (valueRequest.getCustomFieldId() != null) {
                rv.setCustomField(customFieldRepository.findById(valueRequest.getCustomFieldId())
                        .orElseThrow(() -> new IllegalArgumentException("Custom field not found: " + valueRequest.getCustomFieldId())));
            }
            savedValues.add(reportValueRepository.save(rv));
        }

        log.info("Report {} submitted for vessel {} (type: {})", report.getId(), vesselId, reportType.getName());
        return reportMapper.toDto(report, savedValues);
    }

    @Transactional(readOnly = true)
    public List<ReportDto> query(Long vesselId, Long reportTypeId, LocalDateTime from, LocalDateTime to) {
        List<Report> reports;
        if (reportTypeId != null) {
            reports = reportRepository.findByVesselIdAndReportTypeIdAndSubmittedAtBetweenOrderBySubmittedAtDesc(
                    vesselId, reportTypeId, from, to);
        } else {
            reports = reportRepository.findByVesselIdAndSubmittedAtBetweenOrderBySubmittedAtDesc(vesselId, from, to);
        }
        return reports.stream()
                .map(r -> reportMapper.toDto(r, reportValueRepository.findByReportId(r.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public ReportDto findById(Long id) {
        return reportRepository.findById(id)
                .map(r -> reportMapper.toDto(r, reportValueRepository.findByReportId(r.getId())))
                .orElse(null);
    }
}
