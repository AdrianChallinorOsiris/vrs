package uk.co.osiris.vrs.consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumerReportRepository extends JpaRepository<ConsumerReport, Long> {

    List<ConsumerReport> findByFleetId(Long fleetId);
}
