package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.application.usecase.CreateNewUserUseCase;
import cm.klg.service_request.application.usecase.CreateServiceProviderUseCase;
import cm.klg.service_request.application.usecase.DeleteUserUseCase;
import cm.klg.service_request.application.usecase.RejectServiceProviderUseCase;
import cm.klg.service_request.application.usecase.UpdateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingInboundSpringBeans {

  @Bean
  public CreateUserInboundEventHandler createUserInboundEventHandler(
      CreateNewUserUseCase createNewUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new CreateUserInboundEventHandler(createNewUserUseCase, messagingInboundMapper);
  }

  @Bean
  public UpdateUserInboundEventHandler updateUserInboundEventHandler(
      UpdateUserUseCase updateUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new UpdateUserInboundEventHandler(updateUserUseCase, messagingInboundMapper);
  }

  @Bean
  public DeleteUserInboundEventHandler deleteUserInboundEventHandler(
      DeleteUserUseCase deleteUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new DeleteUserInboundEventHandler(deleteUserUseCase, messagingInboundMapper);
  }

  @Bean
  public ApproveServiceProviderInboundEventHandler approveServiceProviderInboundEventHandler(
      ApproveServiceProviderUseCase approveServiceProviderUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new ApproveServiceProviderInboundEventHandler(
        approveServiceProviderUseCase, messagingInboundMapper);
  }

  @Bean
  public CreateServiceProviderInboundEventHandler createServiceProviderInboundEventHandler(
      CreateServiceProviderUseCase createServiceProviderUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new CreateServiceProviderInboundEventHandler(
        createServiceProviderUseCase, messagingInboundMapper);
  }

  @Bean
  public RejectServiceProviderInboundEventHandler rejectServiceProviderInboundEventHandler(
      RejectServiceProviderUseCase rejectServiceProviderUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new RejectServiceProviderInboundEventHandler(
        rejectServiceProviderUseCase, messagingInboundMapper);
  }
}
