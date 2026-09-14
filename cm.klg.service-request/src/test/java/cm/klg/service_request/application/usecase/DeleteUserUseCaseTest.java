package cm.klg.service_request.application.usecase;

import static org.mockito.Mockito.verify;

import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.domain.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private DeleteUserUseCase deleteUserUseCase;

  @Test
  void shouldDeleteUser() {
    UserId userId = UserId.from(UUID.randomUUID());

    deleteUserUseCase.execute(userId);

    verify(userRepository).delete(userId);
  }
}
