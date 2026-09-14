package cm.klg.service_request.application.usecase;

import static cm.klg.common.base.domain.CreatedAt.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserNotFoundException;
import cm.klg.service_request.domain.user.UserProfile;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UpdateUserUseCase updateUserUseCase;

  @Test
  void shouldUpdateUser() {
    UserId userId = UserId.from(UUID.randomUUID());
    User existingUser =
        User.reconstitute(
            userId,
            new UserProfile(
                null, Lastname.from("Old"), null, PhoneNumber.from("+237", "600000000")),
            false,
            from(java.time.LocalDateTime.now()));

    when(userRepository.loadById(userId)).thenReturn(Optional.of(existingUser));

    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            userId,
            Lastname.from("Doe"),
            Firstname.from("John"),
            EmailAddress.from("john.doe@example.com"),
            PhoneNumber.from("+237", "699999999"));

    updateUserUseCase.execute(command);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).update(userCaptor.capture());

    assertThat(userCaptor.getValue())
        .satisfies(
            user -> {
              assertThat(user.getId()).isEqualTo(userId);
              assertThat(user.getLastname().value()).isEqualTo("Doe");
              assertThat(user.getFirstname()).isNotNull();
              assertThat(user.getFirstname().value()).isEqualTo("John");
              assertThat(user.getEmail()).isNotNull();
              assertThat(user.getEmail().value()).isEqualTo("john.doe@example.com");
              assertThat(user.getPhoneNumber().countryCode()).isEqualTo("+237");
              assertThat(user.getPhoneNumber().number()).isEqualTo("699999999");
            });
  }

  @Test
  void shouldThrowWhenUserNotFound() {
    UserId userId = UserId.from(UUID.randomUUID());
    when(userRepository.loadById(userId)).thenReturn(Optional.empty());

    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            userId,
            Lastname.from("Doe"),
            Firstname.from("John"),
            EmailAddress.from("john.doe@example.com"),
            PhoneNumber.from("+237", "699999999"));

    assertThatThrownBy(() -> updateUserUseCase.execute(command))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void shouldUpdateUserWithNullableFirstnameAndEmail() {
    UserId userId = UserId.from(UUID.randomUUID());
    User existingUser =
        User.reconstitute(
            userId,
            new UserProfile(
                null, Lastname.from("Old"), null, PhoneNumber.from("+237", "600000000")),
            false,
            from(java.time.LocalDateTime.now()));

    when(userRepository.loadById(userId)).thenReturn(Optional.of(existingUser));

    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            userId, Lastname.from("Doe"), null, null, PhoneNumber.from("+237", "699999999"));

    updateUserUseCase.execute(command);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).update(userCaptor.capture());

    assertThat(userCaptor.getValue().getFirstname()).isNull();
    assertThat(userCaptor.getValue().getEmail()).isNull();
  }
}
