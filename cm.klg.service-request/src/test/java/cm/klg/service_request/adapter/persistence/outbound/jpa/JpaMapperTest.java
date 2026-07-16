package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestDetails;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestParties;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import cm.klg.service_request.domain.service_request.ServiceTypeId;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.IdentityId;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JpaMapperTest {

  private final JpaMapper mapper = new JpaMapperImpl();

  @Test
  void shouldMapUserToJpaTest() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.of(2026, 5, 31, 12, 0);
    User user =
        User.reconstitute(
            UserId.from(id),
            IdentityId.from(id),
            new UserProfile(
                Firstname.from("John"),
                Lastname.from("Doe"),
                EmailAddress.from("johndoe@example.com"),
                new PhoneNumber("+237", "699999999")),
            true,
            CreatedAt.from(createdAt));

    var resultUnderTest = mapper.toUserJpa(user);

    assertThat(resultUnderTest)
        .satisfies(
            userJpa -> {
              assertThat(userJpa.getId()).isEqualTo(id);
              assertThat(userJpa.getFirstname()).isEqualTo("John");
              assertThat(userJpa.getLastname()).isEqualTo("Doe");
              assertThat(userJpa.getEmailAddress()).isEqualTo("johndoe@example.com");
              assertThat(userJpa.getPhoneNumber().getCountryCode()).isEqualTo("+237");
              assertThat(userJpa.getPhoneNumber().getNumber()).isEqualTo("699999999");
              assertThat(userJpa.getCreatedAt()).isEqualTo(createdAt);
            });
  }

  @Test
  void shouldMapUserJpaToDomainTest() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.of(2026, 5, 31, 12, 30);
    UserJpa userJpa = new UserJpa();
    userJpa.setId(id);
    userJpa.setFirstname("John");
    userJpa.setLastname("Doe");
    userJpa.setEmailAddress("john.doe@example.com");
    userJpa.setPhoneNumber(new PhoneNumberJpa("+237", "699999999"));
    userJpa.setServiceProvider(true);
    userJpa.setCreatedAt(createdAt);

    var resultUnderTest = mapper.toDomain(userJpa);

    assertThat(resultUnderTest)
        .satisfies(
            user -> {
              assertThat(user.getId().value()).isEqualTo(id);
              assertThat(user.getFirstname().value()).isEqualTo("John");
              assertThat(user.getLastname().value()).isEqualTo("Doe");
              assertThat(user.getEmail().value()).isEqualTo("john.doe@example.com");
              assertThat(user.getPhoneNumber().countryCode()).isEqualTo("+237");
              assertThat(user.getPhoneNumber().number()).isEqualTo("699999999");
              assertThat(user.isServiceProvider()).isTrue();
              assertThat(user.getCreatedAt().value()).isEqualTo(createdAt);
            });
  }

  @Test
  void shouldMapServiceProviderToJpaTest() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    LocalDateTime approvedAt = LocalDateTime.of(2026, 5, 31, 13, 0);
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            new ServiceProviderId(id),
            IdentityId.from(userId),
            ServiceProviderStatus.APPROVED,
            CreatedAt.from(approvedAt));

    var resultUnderTest = mapper.toJpa(serviceProvider);

    assertThat(resultUnderTest)
        .satisfies(
            jpa -> {
              assertThat(jpa.getId()).isEqualTo(id);
              assertThat(jpa.getUserId()).isEqualTo(userId);
              assertThat(jpa.getStatus()).isEqualTo("APPROVED");
              assertThat(jpa.getApprovedAt()).isEqualTo(approvedAt);
            });
  }

  @Test
  void shouldMapNullServiceProviderToNullTest() {
    assertThat(mapper.toJpa((ServiceProvider) null)).isNull();
  }

  @Test
  void shouldMapServiceRequestToJpaWithProvidedOptionalFieldsTest() {
    UUID userId = UUID.randomUUID();
    UUID providerId = UUID.randomUUID();
    UUID serviceTypeId = UUID.randomUUID();
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(
                IdentityId.from(userId),
                new ServiceProviderId(providerId),
                ServiceTypeId.from(serviceTypeId)),
            new ServiceRequestDetails(
                ServiceRequestTitle.from("title"),
                ServiceRequestDescription.from("description"),
                ServiceRequestLocation.from("location")));

    ServiceRequestJpa resultUnderTest = mapper.toJpa(serviceRequest);

    assertThat(resultUnderTest)
        .satisfies(
            jpa -> {
              assertThat(jpa.getId()).isEqualTo(serviceRequest.getId().value());
              assertThat(jpa.getUserId()).isEqualTo(userId);
              assertThat(jpa.getProviderId()).isEqualTo(providerId);
              assertThat(jpa.getServiceTypeId()).isEqualTo(serviceTypeId);
              assertThat(jpa.getTitle()).isEqualTo("title");
              assertThat(jpa.getDescription()).isEqualTo("description");
              assertThat(jpa.getLocation()).isEqualTo("location");
              assertThat(jpa.getStatus()).isEqualTo(ServiceRequestStatus.PENDING.name());
              assertThat(jpa.getCreatedAt()).isEqualTo(serviceRequest.getCreatedAt().value());
            });
  }

  @Test
  void shouldMapServiceRequestToJpaWithAbsentOptionalFieldsTest() {
    ServiceRequest serviceRequest =
        ServiceRequest.of(
            new ServiceRequestParties(
                IdentityId.from(UUID.randomUUID()),
                new ServiceProviderId(UUID.randomUUID()),
                ServiceTypeId.from(UUID.randomUUID())),
            new ServiceRequestDetails(
                ServiceRequestTitle.NULL,
                ServiceRequestDescription.NULL,
                ServiceRequestLocation.NULL));

    ServiceRequestJpa resultUnderTest = mapper.toJpa(serviceRequest);

    assertThat(resultUnderTest)
        .satisfies(
            jpa -> {
              assertThat(jpa.getTitle()).isNull();
              assertThat(jpa.getDescription()).isNull();
              assertThat(jpa.getLocation()).isNull();
            });
  }

  @Test
  void shouldMapNullServiceRequestToNullTest() {
    assertThat(mapper.toJpa((ServiceRequest) null)).isNull();
  }
}
