package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderNotFoundException;
import cm.klg.service_request.domain.user.IdentityId;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);

  boolean existsById(ServiceProviderId serviceProviderId);

  ServiceProvider loadByUserId(IdentityId userId) throws ServiceProviderNotFoundException;
}
