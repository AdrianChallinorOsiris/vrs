package uk.co.osiris.vrs.consumer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uk.co.osiris.vrs.report.FleetCustomField;
import uk.co.osiris.vrs.report.ReportFieldDefinition;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumer_report_field", schema = "vrs")
@Getter
@Setter
public class ConsumerReportField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_report_id", nullable = false)
    private ConsumerReport consumerReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_definition_id")
    private ReportFieldDefinition fieldDefinition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_field_id")
    private FleetCustomField customField;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    @Column(name = "format_hint", length = 100)
    private String formatHint;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsumerReportField other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
