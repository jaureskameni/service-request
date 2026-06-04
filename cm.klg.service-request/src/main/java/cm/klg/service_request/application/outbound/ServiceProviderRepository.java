package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);

  boolean existsById(ServiceProviderId serviceProviderId);
}
