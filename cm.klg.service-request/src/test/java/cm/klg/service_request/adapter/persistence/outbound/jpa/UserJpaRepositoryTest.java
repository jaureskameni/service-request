package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.IdentityId;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserJpaRepositoryTest {

  @Mock private UserSpringRepository userSpringRepository;
  @Mock private JpaMapper jpaMapper;

  @InjectMocks private UserJpaRepository repository;

  @Test
  void shouldInsertIfAbsentMappedUser() {
    User user = user();
    UserJpa userJpa = new UserJpa();
    userJpa.setId(UUID.randomUUID());
    userJpa.setIdentityId(UUID.randomUUID());
    userJpa.setFirstname("John");
    userJpa.setLastname("Doe");
    userJpa.setEmailAddress("john.doe@example.com");
    userJpa.setPhoneNumber(new PhoneNumberJpa("+237", "699999999"));
    userJpa.setServiceProvider(false);
    userJpa.setCreatedAt(LocalDateTime.now());
    when(jpaMapper.toUserJpa(user)).thenReturn(userJpa);
    when(userSpringRepository.insertIfAbsent(
            any(UUID.class),
            any(UUID.class),
            any(String.class),
            any(String.class),
            any(String.class),
            any(String.class),
            anyBoolean(),
            any(LocalDateTime.class)))
        .thenReturn(1);

    repository.insertIfAbsent(user);

    verify(userSpringRepository)
        .insertIfAbsent(
            eq(userJpa.getId()),
            eq(userJpa.getIdentityId()),
            eq(userJpa.getFirstname()),
            eq(userJpa.getLastname()),
            eq(userJpa.getEmailAddress()),
            any(String.class),
            eq(userJpa.isServiceProvider()),
            eq(userJpa.getCreatedAt()));
  }

  @Test
  void shouldCheckUserExistenceByIdentityId() {
    IdentityId userId = IdentityId.from(UUID.randomUUID());
    when(userSpringRepository.existsByIdentityId(userId.value())).thenReturn(true);

    assertThat(repository.existsByUserId(userId)).isTrue();
  }

  @Test
  void shouldFindUserById() {
    UserId userId = UserId.from(UUID.randomUUID());
    UserJpa userJpa = new UserJpa();
    User user = user();
    when(userSpringRepository.findById(userId.value())).thenReturn(Optional.of(userJpa));
    when(jpaMapper.toDomain(userJpa)).thenReturn(user);

    assertThat(repository.loadById(userId)).contains(user);
  }

  @Test
  void shouldReturnEmptyWhenUserIsNotFound() {
    UserId userId = UserId.from(UUID.randomUUID());
    when(userSpringRepository.findById(userId.value())).thenReturn(Optional.empty());

    assertThat(repository.loadById(userId)).isEmpty();
  }

  @Test
  void shouldLoadUserById() {
    UserId userId = UserId.from(UUID.randomUUID());
    UserJpa userJpa = new UserJpa();
    User user = user();
    when(userSpringRepository.findById(userId.value())).thenReturn(Optional.of(userJpa));
    when(jpaMapper.toDomain(userJpa)).thenReturn(user);

    assertThat(repository.load(userId)).isEqualTo(user);
  }

  @Test
  void shouldThrowWhenLoadingMissingUser() {
    UserId userId = UserId.from(UUID.randomUUID());
    when(userSpringRepository.findById(userId.value())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> repository.load(userId)).isInstanceOf(RuntimeException.class);
  }

  private static User user() {
    return User.reconstitute(
        UserId.from(UUID.randomUUID()),
        IdentityId.from(UUID.randomUUID()),
        new UserProfile(
            Firstname.from("John"),
            Lastname.from("Doe"),
            EmailAddress.from("john.doe@example.com"),
            new PhoneNumber("+237", "699999999")),
        false,
        CreatedAt.from(LocalDateTime.now()));
  }
}
