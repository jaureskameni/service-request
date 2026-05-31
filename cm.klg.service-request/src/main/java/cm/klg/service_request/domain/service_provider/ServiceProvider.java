package cm.klg.service_request.domain.service_provider;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.user.UserId;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceProvider {
  private ServiceProviderId id;
  private UserId userId;
  private ServiceProviderStatus status;
  private CreatedAt createdAt;
  @Nullable private CreatedAt updatedAt;

  public ServiceProvider(
      ServiceProviderId id,
      UserId userId,
      ServiceProviderStatus status,
      CreatedAt createdAt,
      @Nullable CreatedAt updatedAt) {
    this.id = id;
    this.userId = userId;
    this.status = status;
    this.updatedAt = updatedAt;
    this.createdAt = createdAt;
  }

  public static ServiceProvider reconstitute(
      ServiceProviderId id,
      UserId userId,
      ServiceProviderStatus status,
      CreatedAt createdAt,
      CreatedAt updatedAt) {
    return new ServiceProvider(id, userId, status, createdAt, updatedAt);
  }
}
