package cm.klg.service_request.domain.service_request;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
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

  @Test
  void shouldAcceptServiceRequestWhenProviderMatches() {
    ServiceProviderId providerId = ServiceProviderId.generate();
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(
                UserId.from(UUID.randomUUID()), providerId, ServiceTypeId.from(UUID.randomUUID())),
            new ServiceRequestDetails(null, null, null));

    var event = serviceRequest.accept(providerId);

    assertThat(serviceRequest.getStatus()).isEqualTo(ServiceRequestStatus.ACCEPTED);
    assertThat(serviceRequest.getUpdatedAt()).isNotNull();
    assertThat(event).isNotNull();
    assertThat(event.id()).isEqualTo(serviceRequest.getId());
    assertThat(event.userId()).isEqualTo(serviceRequest.getUserId());
    assertThat(event.providerId()).isEqualTo(serviceRequest.getServiceProviderId());
    assertThat(event.status()).isEqualTo(ServiceRequestStatus.ACCEPTED);
  }

  @Test
  void shouldRejectServiceRequestWhenProviderMatches() {
    ServiceProviderId providerId = ServiceProviderId.generate();
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(
                UserId.from(UUID.randomUUID()), providerId, ServiceTypeId.from(UUID.randomUUID())),
            new ServiceRequestDetails(null, null, null));

    String reasonValue = "Not available";
    ServiceRequestReason reason = ServiceRequestReason.from(reasonValue);
    var event = serviceRequest.reject(providerId, reason);

    assertThat(serviceRequest.getStatus()).isEqualTo(ServiceRequestStatus.REJECTED);
    assertThat(serviceRequest.getReason()).isEqualTo(reason);
    assertThat(serviceRequest.getUpdatedAt()).isNotNull();
    assertThat(event).isNotNull();
    assertThat(event.id()).isEqualTo(serviceRequest.getId());
    assertThat(event.userId()).isEqualTo(serviceRequest.getUserId());
    assertThat(event.providerId()).isEqualTo(serviceRequest.getServiceProviderId());
    assertThat(event.status()).isEqualTo(ServiceRequestStatus.REJECTED);
    assertThat(event.reason()).isEqualTo(reason);
  }

  @Test
  void shouldThrowExceptionWhenAcceptingWithWrongProvider() {
    ServiceProviderId assignedProviderId = ServiceProviderId.generate();
    ServiceProviderId wrongProviderId = ServiceProviderId.generate();
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(
                UserId.from(UUID.randomUUID()),
                assignedProviderId,
                ServiceTypeId.from(UUID.randomUUID())),
            new ServiceRequestDetails(null, null, null));

    org.junit.jupiter.api.Assertions.assertThrows(
        RequestDoesNotBelongToProviderException.class,
        () -> serviceRequest.accept(wrongProviderId));
  }

  @Test
  void shouldCancelServiceRequestWhenUserMatches() {
    UserId userId = UserId.from(UUID.randomUUID());
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(
                userId, ServiceProviderId.generate(), ServiceTypeId.from(UUID.randomUUID())),
            new ServiceRequestDetails(null, null, null));

    var event = serviceRequest.cancel(userId);

    assertThat(serviceRequest.getStatus()).isEqualTo(ServiceRequestStatus.CANCELLED);
    assertThat(serviceRequest.getUpdatedAt()).isNotNull();
    assertThat(event).isNotNull();
    assertThat(event.id()).isEqualTo(serviceRequest.getId());
    assertThat(event.userId()).isEqualTo(userId);
    assertThat(event.status()).isEqualTo(ServiceRequestStatus.CANCELLED);
  }

  @Test
  void shouldThrowExceptionWhenCancellingWithWrongUser() {
    UserId assignedUserId = UserId.from(UUID.randomUUID());
    UserId wrongUserId = UserId.from(UUID.randomUUID());
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(
                assignedUserId,
                ServiceProviderId.generate(),
                ServiceTypeId.from(UUID.randomUUID())),
            new ServiceRequestDetails(null, null, null));

    org.junit.jupiter.api.Assertions.assertThrows(
        RequestDoesNotBelongToUserException.class, () -> serviceRequest.cancel(wrongUserId));
  }

  @Test
  void shouldReconstituteServiceRequest() {
    ServiceRequestId id = ServiceRequestId.generate();
    UserId userId = UserId.from(UUID.randomUUID());
    ServiceProviderId providerId = ServiceProviderId.generate();
    ServiceTypeId serviceTypeId = ServiceTypeId.from(UUID.randomUUID());
    var createdAt = CreatedAt.from(java.time.LocalDateTime.now());
    var updatedAt = cm.klg.service_request.domain.UpdatedAt.from(java.time.LocalDateTime.now());

    ServiceRequest serviceRequest =
        ServiceRequest.reconstitute(
            id,
            new ServiceRequestParties(userId, providerId, serviceTypeId),
            new ServiceRequestDetails(ServiceRequestTitle.from("title"), null, null),
            new ServiceRequestLifecycle(
                ServiceRequestStatus.ACCEPTED,
                updatedAt,
                createdAt,
                ServiceRequestReason.from("some reason")));

    assertThat(serviceRequest.getId()).isEqualTo(id);
    assertThat(serviceRequest.getStatus()).isEqualTo(ServiceRequestStatus.ACCEPTED);
    assertThat(serviceRequest.getReason().value()).isEqualTo("some reason");
  }
}
