package cm.klg.service_request.adapter.persistence.outbound.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_service_request_follow_up")
public class ServiceRequestFollowUpJpa {
  @Id
  @Column(name = "c_id", nullable = false)
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_service_request_id", nullable = false, unique = true)
  private ServiceRequestJpa serviceRequest;

  @Column(name = "c_client_agreement")
  private String clientAgreement;

  @Column(name = "c_client_agreement_at")
  private LocalDateTime clientAgreementAt;

  @Column(name = "c_provider_agreement")
  private String providerAgreement;

  @Column(name = "c_provider_agreement_at")
  private LocalDateTime providerAgreementAt;

  @Column(name = "c_scheduled_at")
  private LocalDateTime scheduledAt;

  @Column(name = "c_agreement_confirmed_at")
  private LocalDateTime agreementConfirmedAt;

  @Column(name = "c_client_completion")
  private String clientCompletion;

  @Column(name = "c_client_completion_at")
  private LocalDateTime clientCompletionAt;

  @Column(name = "c_provider_completion")
  private String providerCompletion;

  @Column(name = "c_provider_completion_at")
  private LocalDateTime providerCompletionAt;

  public void setServiceRequest(ServiceRequestJpa serviceRequest) {
    this.serviceRequest = serviceRequest;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || getClass() != object.getClass()) return false;
    ServiceRequestFollowUpJpa that = (ServiceRequestFollowUpJpa) object;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
