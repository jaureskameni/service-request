package cm.klg.service_request.domain.service_provider;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.user.UserId;
import lombok.Getter;

@Getter
public class ServiceProvider {
  private final ServiceProviderId id;
  private final UserId userId;
  private final ServiceProviderStatus status;
  private final CreatedAt approvedAt;

  public ServiceProvider(
      ServiceProviderId id, UserId userId, ServiceProviderStatus status, CreatedAt approvedAt) {
    this.id = id;
    this.userId = userId;
    this.status = status;
    this.approvedAt = approvedAt;
  }

  public static ServiceProvider reconstitute(
      ServiceProviderId id, UserId userId, ServiceProviderStatus status, CreatedAt approvedAt) {
    return new ServiceProvider(id, userId, status, approvedAt);
  }
}
