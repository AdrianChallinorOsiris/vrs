package uk.co.osiris.vrs.consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumerReportFieldRepository extends JpaRepository<ConsumerReportField, Long> {

    List<ConsumerReportField> findByConsumerReportIdOrderByDisplayOrder(Long consumerReportId);
}
