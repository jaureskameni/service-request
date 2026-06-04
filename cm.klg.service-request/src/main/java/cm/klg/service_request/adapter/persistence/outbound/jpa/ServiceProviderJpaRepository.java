package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ServiceProviderJpaRepository implements ServiceProviderRepository {
  private final ServiceProviderSpringRepository springRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(@NonNull ServiceProvider serviceProvider) {
    springRepository.save(jpaMapper.toJpa(serviceProvider));
  }

  @Override
  public boolean existsById(@NonNull ServiceProviderId serviceProviderId) {
    return springRepository.existsById(serviceProviderId.value());
  }
}
