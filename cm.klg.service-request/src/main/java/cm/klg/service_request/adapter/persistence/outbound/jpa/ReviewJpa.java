package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.AggregateRootEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "t_review")
public class ReviewJpa extends AggregateRootEntity<UUID> {
  @Id
  @Column(name = "c_id", nullable = false)
  private UUID id;

  @Column(name = "c_service_request_id", nullable = false, unique = true)
  private UUID serviceRequestId;

  @Column(name = "c_client_id", nullable = false)
  private UUID clientId;

  @Column(name = "c_provider_id", nullable = false)
  private UUID providerId;

  @Column(name = "c_rating", nullable = false)
  private int rating;

  @Column(name = "c_comment")
  private String comment;

  @Column(name = "c_status", nullable = false)
  private String status;

  @Column(name = "c_moderation_reason")
  private String moderationReason;

  @Column(name = "c_moderated_by")
  private UUID moderatedBy;

  @Column(name = "c_moderated_at")
  private LocalDateTime moderatedAt;

  @Column(name = "c_created_at", nullable = false)
  private LocalDateTime createdAt;

  @Override
  public boolean equals(Object object) {
    if (object == null || getClass() != object.getClass()) return false;
    ReviewJpa that = (ReviewJpa) object;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
