package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class JpaEntityTest {

  @Test
  void usersShouldBeEqualWhenTheyHaveSameId() {
    UUID id = UUID.randomUUID();
    UserJpa first = new UserJpa();
    first.setId(id);
    UserJpa second = new UserJpa();
    second.setId(id);

    assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
  }

  @Test
  void usersShouldNotBeEqualWhenClassOrIdDiffers() {
    UserJpa user = new UserJpa();
    user.setId(UUID.randomUUID());
    UserJpa other = new UserJpa();
    other.setId(UUID.randomUUID());

    assertThat(user).isNotEqualTo(other).isNotEqualTo(null).isNotEqualTo(new Object());
  }

  @Test
  void serviceProvidersShouldBeEqualWhenTheyHaveSameId() {
    UUID id = UUID.randomUUID();
    ServiceProviderJpa first = new ServiceProviderJpa();
    first.setId(id);
    ServiceProviderJpa second = new ServiceProviderJpa();
    second.setId(id);

    assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
  }

  @Test
  void serviceProvidersShouldNotBeEqualWhenClassOrIdDiffers() {
    ServiceProviderJpa serviceProvider = new ServiceProviderJpa();
    serviceProvider.setId(UUID.randomUUID());
    ServiceProviderJpa other = new ServiceProviderJpa();
    other.setId(UUID.randomUUID());

    assertThat(serviceProvider).isNotEqualTo(other).isNotEqualTo(null).isNotEqualTo(new Object());
  }

  @Test
  void serviceRequestsShouldBeEqualWhenTheyHaveSameId() {
    UUID id = UUID.randomUUID();
    ServiceRequestJpa first = new ServiceRequestJpa();
    first.setId(id);
    ServiceRequestJpa second = new ServiceRequestJpa();
    second.setId(id);

    assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
  }

  @Test
  void serviceRequestsShouldNotBeEqualWhenClassOrIdDiffers() {
    ServiceRequestJpa serviceRequest = new ServiceRequestJpa();
    serviceRequest.setId(UUID.randomUUID());
    ServiceRequestJpa other = new ServiceRequestJpa();
    other.setId(UUID.randomUUID());

    assertThat(serviceRequest).isNotEqualTo(other).isNotEqualTo(null).isNotEqualTo(new Object());
  }
}
