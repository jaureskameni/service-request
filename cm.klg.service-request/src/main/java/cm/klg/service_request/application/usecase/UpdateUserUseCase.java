package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserNotFoundException;
import cm.klg.service_request.domain.user.UserProfile;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateUserUseCase {
  private final UserRepository userRepository;

  public void execute(UpdateUserCommand command) {
    User user = userRepository.loadById(command.userId()).orElseThrow(UserNotFoundException::new);

    UserProfile userProfile =
        new UserProfile(
            command.firstname(), command.lastname(), command.email(), command.phoneNumber());
    user.updateProfile(userProfile);

    userRepository.update(user);
  }

  public record UpdateUserCommand(
      UserId userId,
      Lastname lastname,
      @Nullable Firstname firstname,
      @Nullable EmailAddress email,
      PhoneNumber phoneNumber) {}
}
