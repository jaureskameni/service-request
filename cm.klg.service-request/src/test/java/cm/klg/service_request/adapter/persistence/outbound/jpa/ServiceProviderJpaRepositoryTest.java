package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.IdentityId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceProviderJpaRepositoryTest {

  @Mock private ServiceProviderSpringRepository serviceProviderSpringRepository;
  @Mock private JpaMapper jpaMapper;

  @InjectMocks private ServiceProviderJpaRepository repository;

  @Test
  void shouldInsertMappedServiceProvider() {
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            new ServiceProviderId(UUID.randomUUID()),
            IdentityId.from(UUID.randomUUID()),
            ServiceProviderStatus.APPROVED,
            CreatedAt.from(LocalDateTime.now()));
    ServiceProviderJpa jpa = new ServiceProviderJpa();
    when(jpaMapper.toJpa(serviceProvider)).thenReturn(jpa);

    repository.insert(serviceProvider);

    verify(serviceProviderSpringRepository).save(jpa);
  }

  @Test
  void shouldCheckServiceProviderExistenceById() {
    ServiceProviderId providerId = ServiceProviderId.from(UUID.randomUUID());
    when(serviceProviderSpringRepository.existsById(providerId.value())).thenReturn(true);

    org.assertj.core.api.Assertions.assertThat(repository.existsById(providerId)).isTrue();
  }
}
