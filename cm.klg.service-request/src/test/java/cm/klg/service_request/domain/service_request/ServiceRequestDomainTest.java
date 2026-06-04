package cm.klg.service_request.domain.service_request;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ServiceRequestDomainTest {

  @Test
  void shouldCreateValueObjectsWhenValuesAreProvided() {
    assertThat(ServiceRequestTitle.from("title").value()).isEqualTo("title");
    assertThat(ServiceRequestDescription.from("description").value()).isEqualTo("description");
    assertThat(ServiceRequestLocation.from("location").value()).isEqualTo("location");
  }

  @Test
  void shouldExposeNullConstantsForAbsentValueObjects() {
    assertThat(ServiceRequestTitle.NULL).isNull();
    assertThat(ServiceRequestDescription.NULL).isNull();
    assertThat(ServiceRequestLocation.NULL).isNull();
  }

  @Test
  void shouldCreateServiceRequestWithProvidedOptionalFields() {
    UserId userId = UserId.from(UUID.randomUUID());
    ServiceProviderId providerId = new ServiceProviderId(UUID.randomUUID());
    ServiceTypeId serviceTypeId = ServiceTypeId.from(UUID.randomUUID());
    ServiceRequestTitle title = ServiceRequestTitle.from("title");
    ServiceRequestDescription description = ServiceRequestDescription.from("description");
    ServiceRequestLocation location = ServiceRequestLocation.from("location");

    ServiceRequest resultUnderTest =
        ServiceRequest.of(
            new ServiceRequestParties(userId, providerId, serviceTypeId),
            new ServiceRequestDetails(title, description, location));

    assertThat(resultUnderTest)
        .satisfies(
            serviceRequest -> {
              assertThat(serviceRequest.getId().value()).isNotNull();
              assertThat(serviceRequest.getUserId()).isEqualTo(userId);
              assertThat(serviceRequest.getServiceProviderId()).isEqualTo(providerId);
              assertThat(serviceRequest.getServiceTypeId()).isEqualTo(serviceTypeId);
              assertThat(serviceRequest.getTitle()).isEqualTo(title);
              assertThat(serviceRequest.getDescription()).isEqualTo(description);
              assertThat(serviceRequest.getLocation()).isEqualTo(location);
              assertThat(serviceRequest.getStatus()).isEqualTo(ServiceRequestStatus.PENDING);
              assertThat(serviceRequest.getCreatedAt().value()).isNotNull();
            });
  }

  @Test
  void shouldCreateServiceRequestWithAbsentOptionalFields() {
    ServiceRequest resultUnderTest =
        ServiceRequest.of(
            new ServiceRequestParties(
                UserId.from(UUID.randomUUID()),
                new ServiceProviderId(UUID.randomUUID()),
                ServiceTypeId.from(UUID.randomUUID())),
            new ServiceRequestDetails(
                ServiceRequestTitle.NULL,
                ServiceRequestDescription.NULL,
                ServiceRequestLocation.NULL));

    assertThat(resultUnderTest)
        .satisfies(
            serviceRequest -> {
              assertThat(serviceRequest.getTitle()).isNull();
              assertThat(serviceRequest.getDescription()).isNull();
              assertThat(serviceRequest.getLocation()).isNull();
            });
  }
}
