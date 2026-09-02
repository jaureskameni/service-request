package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.user.IdentityId;

public interface ServiceProviderRepository {
  void insertIfAbsent(ServiceProvider serviceProvider);

  void update(ServiceProvider serviceProvider);

  boolean existsById(ServiceProviderId serviceProviderId);

  ServiceProvider load(ServiceProviderId serviceProviderId) throws ServiceProviderNotFoundException;

  boolean isApprovedById(ServiceProviderId serviceProviderId);

  boolean isApprovedByUserId(IdentityId userId);

  ServiceProvider loadByUserId(IdentityId userId) throws ServiceProviderNotFoundException;
}
