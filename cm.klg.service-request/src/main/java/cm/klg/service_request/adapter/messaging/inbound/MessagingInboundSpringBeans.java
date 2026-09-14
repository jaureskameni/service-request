package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
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
      CreateNewUserUseCase createNewUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new CreateUserInboundEventHandler(
        createNewUserUseCase, messagingInboundMapper, useCaseExecutor);
  }

  @Bean
  public UpdateUserInboundEventHandler updateUserInboundEventHandler(
      UpdateUserUseCase updateUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new UpdateUserInboundEventHandler(
        updateUserUseCase, messagingInboundMapper, useCaseExecutor);
  }

  @Bean
  public DeleteUserInboundEventHandler deleteUserInboundEventHandler(
      DeleteUserUseCase deleteUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new DeleteUserInboundEventHandler(
        deleteUserUseCase, messagingInboundMapper, useCaseExecutor);
  }

  @Bean
  public ApproveServiceProviderInboundEventHandler approveServiceProviderInboundEventHandler(
      ApproveServiceProviderUseCase approveServiceProviderUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new ApproveServiceProviderInboundEventHandler(
        approveServiceProviderUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public CreateServiceProviderInboundEventHandler createServiceProviderInboundEventHandler(
      CreateServiceProviderUseCase createServiceProviderUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new CreateServiceProviderInboundEventHandler(
        createServiceProviderUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public RejectServiceProviderInboundEventHandler rejectServiceProviderInboundEventHandler(
      RejectServiceProviderUseCase rejectServiceProviderUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new RejectServiceProviderInboundEventHandler(
        rejectServiceProviderUseCase, useCaseExecutor, messagingInboundMapper);
  }
}
