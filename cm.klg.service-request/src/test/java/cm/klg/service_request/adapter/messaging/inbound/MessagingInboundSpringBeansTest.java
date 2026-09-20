package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.application.usecase.CreateNewUserUseCase;
import cm.klg.service_request.application.usecase.CreateServiceProviderUseCase;
import cm.klg.service_request.application.usecase.DeleteUserUseCase;
import cm.klg.service_request.application.usecase.RejectServiceProviderUseCase;
import cm.klg.service_request.application.usecase.UpdateUserUseCase;
import org.junit.jupiter.api.Test;

class MessagingInboundSpringBeansTest {

  private final MessagingInboundSpringBeans beans = new MessagingInboundSpringBeans();

  @Test
  void shouldCreateUserInboundEventHandler() {
    CreateNewUserUseCase createNewUserUseCase = mock(CreateNewUserUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);

    CreateUserInboundEventHandler handler =
        beans.createUserInboundEventHandler(createNewUserUseCase, mapper);

    assertThat(handler.createNewUserUseCase()).isEqualTo(createNewUserUseCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
  }

  @Test
  void shouldUpdateUserInboundEventHandler() {
    UpdateUserUseCase updateUserUseCase = mock(UpdateUserUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);

    UpdateUserInboundEventHandler handler =
        beans.updateUserInboundEventHandler(updateUserUseCase, mapper);

    assertThat(handler.updateUserUseCase()).isEqualTo(updateUserUseCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
  }

  @Test
  void shouldDeleteUserInboundEventHandler() {
    DeleteUserUseCase deleteUserUseCase = mock(DeleteUserUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);

    DeleteUserInboundEventHandler handler =
        beans.deleteUserInboundEventHandler(deleteUserUseCase, mapper);

    assertThat(handler.deleteUserUseCase()).isEqualTo(deleteUserUseCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
  }

  @Test
  void shouldCreateApproveServiceProviderInboundEventHandler() {
    ApproveServiceProviderUseCase useCase = mock(ApproveServiceProviderUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);

    ApproveServiceProviderInboundEventHandler handler =
        beans.approveServiceProviderInboundEventHandler(useCase, mapper);

    assertThat(handler.approveServiceProviderUseCase()).isEqualTo(useCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
  }

  @Test
  void shouldCreateServiceProviderInboundEventHandler() {
    CreateServiceProviderUseCase useCase = mock(CreateServiceProviderUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);

    CreateServiceProviderInboundEventHandler handler =
        beans.createServiceProviderInboundEventHandler(useCase, mapper);

    assertThat(handler.createServiceProviderUseCase()).isEqualTo(useCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
  }

  @Test
  void shouldRejectServiceProviderInboundEventHandler() {
    RejectServiceProviderUseCase useCase = mock(RejectServiceProviderUseCase.class);
    MessagingInboundMapper mapper = mock(MessagingInboundMapper.class);

    RejectServiceProviderInboundEventHandler handler =
        beans.rejectServiceProviderInboundEventHandler(useCase, mapper);

    assertThat(handler.rejectServiceProviderUseCase()).isEqualTo(useCase);
    assertThat(handler.messagingInboundMapper()).isEqualTo(mapper);
  }
}
