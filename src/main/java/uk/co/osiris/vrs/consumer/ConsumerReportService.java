package uk.co.osiris.vrs.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.osiris.vrs.fleet.Fleet;
import uk.co.osiris.vrs.fleet.FleetRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerReportService {

    private final ConsumerReportRepository consumerReportRepository;
    private final ConsumerReportFieldRepository consumerReportFieldRepository;
    private final ConsumerSubscriptionRepository subscriptionRepository;
    private final FleetRepository fleetRepository;
    private final ConsumerReportMapper consumerReportMapper;

    @Transactional(readOnly = true)
    public ConsumerReportDto findById(Long id) {
        return consumerReportRepository.findById(id)
                .map(r -> consumerReportMapper.toDto(r, consumerReportFieldRepository.findByConsumerReportIdOrderByDisplayOrder(id)))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ConsumerReportDto> findByFleet(Long fleetId) {
        return consumerReportRepository.findByFleetId(fleetId).stream()
                .map(r -> consumerReportMapper.toDto(r, consumerReportFieldRepository.findByConsumerReportIdOrderByDisplayOrder(r.getId())))
                .toList();
    }

    @Transactional
    public ConsumerReportDto create(ConsumerReportRequestDto request) {
        Fleet fleet = fleetRepository.findById(request.getFleetId())
                .orElseThrow(() -> new IllegalArgumentException("Fleet not found: " + request.getFleetId()));
        ConsumerReport report = consumerReportMapper.toEntity(request, fleet);
        report = consumerReportRepository.save(report);
        log.info("Created consumer report '{}' for fleet {}", report.getName(), fleet.getId());
        return consumerReportMapper.toDto(report, List.of());
    }

    @Transactional
    public ConsumerReportDto update(Long id, ConsumerReportRequestDto request) {
        ConsumerReport report = consumerReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consumer report not found: " + id));
        report.setName(request.getName());
        report.setDescription(request.getDescription());
        report = consumerReportRepository.save(report);
        return consumerReportMapper.toDto(report, consumerReportFieldRepository.findByConsumerReportIdOrderByDisplayOrder(id));
    }

    @Transactional
    public void delete(Long id) {
        if (!consumerReportRepository.existsById(id)) {
            throw new IllegalArgumentException("Consumer report not found: " + id);
        }
        consumerReportRepository.deleteById(id);
    }

    @Transactional
    public ConsumerSubscription addSubscription(Long consumerReportId, ConsumerSubscriptionRequestDto request) {
        ConsumerReport report = consumerReportRepository.findById(consumerReportId)
                .orElseThrow(() -> new IllegalArgumentException("Consumer report not found: " + consumerReportId));
        ConsumerSubscription subscription = new ConsumerSubscription();
        subscription.setConsumerReport(report);
        subscription.setDeliveryMethod(request.getDeliveryMethod());
        subscription.setConfig(request.getConfig());
        return subscriptionRepository.save(subscription);
    }

    @Transactional(readOnly = true)
    public List<ConsumerSubscription> getSubscriptions(Long consumerReportId) {
        return subscriptionRepository.findByConsumerReportIdAndActiveTrue(consumerReportId);
    }
}
