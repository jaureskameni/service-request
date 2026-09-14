package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.application.usecase.CreateNewUserUseCase;
import cm.klg.service_request.application.usecase.DeleteUserUseCase;
import cm.klg.service_request.application.usecase.UpdateUserUseCase;
import org.junit.jupiter.api.Test;

class MessagingInboundSpringBeansTest {

  private final MessagingInboundSpringBeans beans = new MessagingInboundSpringBeans();

  @Test
  void shouldCreateUserInboundEventHandler() {
    CreateNewUserUseCase createNewUserUseCase = mock(CreateNewUserUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);
    UseCaseExecutor useCaseExecutor = mock(UseCaseExecutor.class);

    CreateUserInboundEventHandler handler =
        beans.createUserInboundEventHandler(createNewUserUseCase, mapper, useCaseExecutor);

    assertThat(handler.createNewUserUseCase()).isEqualTo(createNewUserUseCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
    assertThat(handler.useCaseExecutor()).isEqualTo(useCaseExecutor);
  }

  @Test
  void shouldUpdateUserInboundEventHandler() {
    UpdateUserUseCase updateUserUseCase = mock(UpdateUserUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);
    UseCaseExecutor useCaseExecutor = mock(UseCaseExecutor.class);

    UpdateUserInboundEventHandler handler =
        beans.updateUserInboundEventHandler(updateUserUseCase, mapper, useCaseExecutor);

    assertThat(handler.updateUserUseCase()).isEqualTo(updateUserUseCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
    assertThat(handler.useCaseExecutor()).isEqualTo(useCaseExecutor);
  }

  @Test
  void shouldDeleteUserInboundEventHandler() {
    DeleteUserUseCase deleteUserUseCase = mock(DeleteUserUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);
    UseCaseExecutor useCaseExecutor = mock(UseCaseExecutor.class);

    DeleteUserInboundEventHandler handler =
        beans.deleteUserInboundEventHandler(deleteUserUseCase, mapper, useCaseExecutor);

    assertThat(handler.deleteUserUseCase()).isEqualTo(deleteUserUseCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
    assertThat(handler.useCaseExecutor()).isEqualTo(useCaseExecutor);
  }

  @Test
  void shouldCreateApproveServiceProviderInboundEventHandler() {
    ApproveServiceProviderUseCase useCase = mock(ApproveServiceProviderUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);
    UseCaseExecutor useCaseExecutor = mock(UseCaseExecutor.class);

    ApproveServiceProviderInboundEventHandler handler =
        beans.approveServiceProviderInboundEventHandler(useCase, mapper, useCaseExecutor);

    assertThat(handler.approveServiceProviderUseCase()).isEqualTo(useCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
    assertThat(handler.useCaseExecutor()).isEqualTo(useCaseExecutor);
  }
}
