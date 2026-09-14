package cm.klg.service_request.application.usecase;

import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCase {
  private final UserRepository userRepository;

  public void execute(UserId userId) {
    userRepository.delete(userId);
  }
}
