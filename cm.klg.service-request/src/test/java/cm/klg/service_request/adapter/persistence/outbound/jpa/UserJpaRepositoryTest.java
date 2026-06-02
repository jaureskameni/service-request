package cm.klg.service_request.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
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
  void shouldInsertMappedUser() {
    User user = user();
    UserJpa userJpa = new UserJpa();
    when(jpaMapper.toJpa(user)).thenReturn(userJpa);

    repository.insert(user);

    verify(userSpringRepository).save(userJpa);
  }

  @Test
  void shouldCheckUserExistenceById() {
    UserId userId = UserId.from(UUID.randomUUID());
    when(userSpringRepository.existsById(userId.value())).thenReturn(true);

    assertThat(repository.existsById(userId)).isTrue();
  }

  @Test
  void shouldFindUserById() {
    UserId userId = UserId.from(UUID.randomUUID());
    UserJpa userJpa = new UserJpa();
    User user = user();
    when(userSpringRepository.findById(userId.value())).thenReturn(Optional.of(userJpa));
    when(jpaMapper.toDomain(userJpa)).thenReturn(user);

    assertThat(repository.findById(userId)).contains(user);
  }

  @Test
  void shouldReturnEmptyWhenUserIsNotFound() {
    UserId userId = UserId.from(UUID.randomUUID());
    when(userSpringRepository.findById(userId.value())).thenReturn(Optional.empty());

    assertThat(repository.findById(userId)).isEmpty();
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
        new UserProfile(
            Firstname.from("John"),
            Lastname.from("Doe"),
            EmailAddress.from("john.doe@example.com"),
            new PhoneNumber("+237", "699999999")),
        false,
        CreatedAt.from(LocalDateTime.now()));
  }
}
