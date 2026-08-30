package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.mockito.Mockito.when;

import cm.klg.service_request.domain.service_provider.ServiceProviderId;
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
  void shouldCheckServiceProviderExistenceById() {
    ServiceProviderId providerId = ServiceProviderId.from(UUID.randomUUID());
    when(serviceProviderSpringRepository.existsById(providerId.value())).thenReturn(true);

    org.assertj.core.api.Assertions.assertThat(repository.existsById(providerId)).isTrue();
  }
}
