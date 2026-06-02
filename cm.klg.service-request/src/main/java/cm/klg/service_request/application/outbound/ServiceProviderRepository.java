package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.service_provider.ServiceProvider;

public interface ServiceProviderRepository {
  void insert(ServiceProvider serviceProvider);
}
