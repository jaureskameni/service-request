package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
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
  void shouldMapUserToJpa() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.of(2026, 5, 31, 12, 0);
    User user =
        User.reconstitute(
            UserId.from(id),
            new UserProfile(
                Firstname.from("John"),
                Lastname.from("Doe"),
                EmailAddress.from("johndoe@example.com"),
                new PhoneNumber("+237", "699999999")),
            true,
            CreatedAt.from(createdAt));

    UserJpa userJpa = mapper.toJpa(user);

    assertThat(userJpa.getId()).isEqualTo(id);
    assertThat(userJpa.getFirstname()).isEqualTo("John");
    assertThat(userJpa.getLastname()).isEqualTo("Doe");
    assertThat(userJpa.getEmailAddress()).isEqualTo("johndoe@example.com");
    assertThat(userJpa.getPhoneNumber().getCountryCode()).isEqualTo("+237");
    assertThat(userJpa.getPhoneNumber().getNumber()).isEqualTo("699999999");
    assertThat(userJpa.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldMapNullUserToNull() {
    assertThat(mapper.toJpa((User) null)).isNull();
  }

  @Test
  void shouldMapUserJpaToDomain() {
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

    User user = mapper.toDomain(userJpa);

    assertThat(user.getId().value()).isEqualTo(id);
    assertThat(user.getFirstname().value()).isEqualTo("John");
    assertThat(user.getLastname().value()).isEqualTo("Doe");
    assertThat(user.getEmail().value()).isEqualTo("john.doe@example.com");
    assertThat(user.getPhoneNumber().countryCode()).isEqualTo("+237");
    assertThat(user.getPhoneNumber().number()).isEqualTo("699999999");
    assertThat(user.isServiceProvider()).isTrue();
    assertThat(user.getCreatedAt().value()).isEqualTo(createdAt);
  }

  @Test
  void shouldMapServiceProviderToJpa() {
    UUID id = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    LocalDateTime approvedAt = LocalDateTime.of(2026, 5, 31, 13, 0);
    ServiceProvider serviceProvider =
        ServiceProvider.reconstitute(
            new ServiceProviderId(id),
            new UserId(userId),
            ServiceProviderStatus.APPROVED,
            CreatedAt.from(approvedAt));

    ServiceProviderJpa jpa = mapper.toJpa(serviceProvider);

    assertThat(jpa.getId()).isEqualTo(id);
    assertThat(jpa.getUserId()).isEqualTo(userId);
    assertThat(jpa.getStatus()).isEqualTo("APPROVED");
    assertThat(jpa.getApprovedAt()).isEqualTo(approvedAt);
  }

  @Test
  void shouldMapNullServiceProviderToNull() {
    assertThat(mapper.toJpa((ServiceProvider) null)).isNull();
  }
}
