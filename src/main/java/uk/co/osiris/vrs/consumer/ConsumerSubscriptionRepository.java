package uk.co.osiris.vrs.consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumerSubscriptionRepository extends JpaRepository<ConsumerSubscription, Long> {

    List<ConsumerSubscription> findByConsumerReportIdAndActiveTrue(Long consumerReportId);
}
