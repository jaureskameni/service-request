package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ServiceProviderJpaRepository implements ServiceProviderRepository {
  private final ServiceProviderSpringRepository serviceProviderSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(ServiceProvider serviceProvider) {
    serviceProviderSpringRepository.save(jpaMapper.toJpa(serviceProvider));
  }
}
