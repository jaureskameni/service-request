package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.AggregateRootEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@SuppressWarnings("JpaDataSourceORMInspection")
@FieldNameConstants
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_service_request")
public class ServiceRequestJpa extends AggregateRootEntity<UUID> {

  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_client_id")
  private UUID clientId;

  @Column(name = "c_provider_id")
  private UUID providerId;

  @Column(name = "c_service_type_id")
  private UUID serviceTypeId;

  @Column(name = "c_title")
  private String title;

  @Column(name = "c_description")
  private String description;

  @Column(name = "c_location")
  private String location;

  @Column(name = "c_status")
  private String status;

  @Column(name = "c_reason")
  private String reason;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Column(name = "c_updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "c_accepted_at")
  private LocalDateTime acceptedAt;

  @OneToOne(
      mappedBy = "serviceRequest",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private ServiceRequestFollowUpJpa followUp;

  public void setFollowUp(ServiceRequestFollowUpJpa followUp) {
    this.followUp = followUp;
    if (followUp != null && followUp.getServiceRequest() != this) {
      followUp.setServiceRequest(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    ServiceRequestJpa that = (ServiceRequestJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
