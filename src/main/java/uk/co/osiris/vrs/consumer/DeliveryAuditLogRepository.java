package uk.co.osiris.vrs.consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAuditLogRepository extends JpaRepository<DeliveryAuditLog, Long> {

    List<DeliveryAuditLog> findByConsumerReportIdAndVesselIdOrderByDeliveredAtDesc(Long consumerReportId, Long vesselId);
}
