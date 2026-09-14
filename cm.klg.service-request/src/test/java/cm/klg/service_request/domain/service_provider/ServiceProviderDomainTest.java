package cm.klg.service_request.domain.service_provider;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.user.UserId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ServiceProviderDomainTest {

  @Test
  void shouldCreateIdentifiers() {
    UUID cityId = UUID.randomUUID();
    UUID districtId = UUID.randomUUID();
    UUID quarterId = UUID.randomUUID();

    assertThat(ServiceProviderId.generate().value()).isNotNull();
    assertThat(UserCityId.from(cityId).value()).isEqualTo(cityId);
    assertThat(UserDistrictId.from(districtId).value()).isEqualTo(districtId);
    assertThat(UserQuarterId.from(quarterId).value()).isEqualTo(quarterId);
  }

  @Test
  void shouldCreateProviderLocationWithNullableQuarter() {
    UserCityId cityId = UserCityId.from(UUID.randomUUID());
    UserDistrictId districtId = UserDistrictId.from(UUID.randomUUID());
    UserQuarterId quarterId = UserQuarterId.from(UUID.randomUUID());

    ProviderLocation completeLocation = new ProviderLocation(cityId, districtId, quarterId);
    ProviderLocation locationWithoutQuarter = new ProviderLocation(cityId, districtId, null);

    assertThat(completeLocation.getCityId()).isEqualTo(cityId);
    assertThat(completeLocation.getDistrictId()).isEqualTo(districtId);
    assertThat(completeLocation.getQuarterId()).isEqualTo(quarterId);
    assertThat(locationWithoutQuarter.getQuarterId()).isNull();
  }

  @Test
  void shouldReconstituteServiceProvider() {
    ServiceProviderId id = ServiceProviderId.from(UUID.randomUUID());
    UserId userId = UserId.from(UUID.randomUUID());
    CreatedAt approvedAt = CreatedAt.from(LocalDateTime.now());

    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(id, userId, ServiceProviderStatus.APPROVED, approvedAt);

    assertThat(serviceProvider.getId()).isEqualTo(id);
    assertThat(serviceProvider.getUserId()).isEqualTo(userId);
    assertThat(serviceProvider.getStatus()).isEqualTo(ServiceProviderStatus.APPROVED);
    assertThat(serviceProvider.getApprovedAt()).isEqualTo(approvedAt);
  }
}
