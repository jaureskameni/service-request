package cm.klg.service_request.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserDomainTest {

  @Test
  void shouldCreateFirstnameFromTrimmedValue() {
    assertThat(Firstname.from(" John ").value()).isEqualTo("John");
  }

  @Test
  void shouldReturnNullFirstnameFromNullValue() {
    assertThat(Firstname.from(null)).isNull();
  }

  @Test
  void shouldRejectBlankFirstname() {
    assertThatThrownBy(() -> Firstname.from(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Firstname cannot be blank");
  }

  @Test
  void shouldCreateSimpleValueObjects() {
    UUID id = UUID.randomUUID();

    assertThat(UserId.from(id).value()).isEqualTo(id);
    assertThat(Lastname.from("Doe").value()).isEqualTo("Doe");
    assertThat(EmailAddress.from("john.doe@example.com").value()).isEqualTo("john.doe@example.com");
    assertThat(EmailAddress.from(null).value()).isNull();
  }

  @Test
  void shouldReconstituteUser() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    UserProfile profile =
        new UserProfile(
            Firstname.from("John"),
            Lastname.from("Doe"),
            EmailAddress.from("john.doe@example.com"),
            new PhoneNumber("+237", "699999999"));

    User user = User.reconstitute(UserId.from(id), profile, true, CreatedAt.from(createdAt));

    assertThat(user.getId().value()).isEqualTo(id);
    assertThat(user.getFirstname().value()).isEqualTo("John");
    assertThat(user.getLastname().value()).isEqualTo("Doe");
    assertThat(user.getEmail().value()).isEqualTo("john.doe@example.com");
    assertThat(user.getPhoneNumber().countryCode()).isEqualTo("+237");
    assertThat(user.getPhoneNumber().number()).isEqualTo("699999999");
    assertThat(user.isServiceProvider()).isTrue();
    assertThat(user.getCreatedAt().value()).isEqualTo(createdAt);
  }
}
