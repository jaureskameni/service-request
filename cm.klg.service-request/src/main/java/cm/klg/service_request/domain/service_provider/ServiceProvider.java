package cm.klg.service_request.domain.service_provider;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.user.UserId;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceProvider {
  private final ServiceProviderId id;
  private final UserId userId;
  private ServiceProviderStatus status;
  private @Nullable CreatedAt approvedAt;

  public ServiceProvider(
      ServiceProviderId id,
      UserId userId,
      ServiceProviderStatus status,
      @Nullable CreatedAt approvedAt) {
    this.id = id;
    this.userId = userId;
    this.status = status;
    this.approvedAt = approvedAt;
  }

  public static ServiceProvider reconstitute(
      ServiceProviderId id,
      UserId userId,
      ServiceProviderStatus status,
      @Nullable CreatedAt approvedAt) {
    return new ServiceProvider(id, userId, status, approvedAt);
  }

  public void approve(CreatedAt approvedAt) {
    this.status = ServiceProviderStatus.APPROVED;
    this.approvedAt = approvedAt;
  }

  public void reject() {
    this.status = ServiceProviderStatus.REJECTED;
    this.approvedAt = null;
  }
}
