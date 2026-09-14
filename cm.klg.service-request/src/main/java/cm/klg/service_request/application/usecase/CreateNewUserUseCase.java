package cm.klg.service_request.application.usecase;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class CreateNewUserUseCase {
  private final UserRepository userRepository;

  public void execute(CreateNewUserCommand command) {
    EmailAddress emailAddress = EmailAddress.from(command.email());
    Firstname firstname = Firstname.from(command.firstname());
    PhoneNumber phoneNumber = new PhoneNumber(command.countryCode(), command.phoneNumber());
    Lastname lastname = Lastname.from(command.lastname());

    UserProfile userProfile = new UserProfile(firstname, lastname, emailAddress, phoneNumber);
    User newUser =
        User.reconstitute(
            UserId.from(command.id()), userProfile, false, CreatedAt.from(command.createdAt));

    userRepository.insertIfAbsent(newUser);
  }

  public record CreateNewUserCommand(
      UUID id,
      String lastname,
      @Nullable String firstname,
      @Nullable String email,
      String countryCode,
      String phoneNumber,
      LocalDateTime createdAt) {}
}
