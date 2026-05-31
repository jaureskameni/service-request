package cm.klg.service_request.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.service_request.application.usecase.CreateNewUserUseCase;
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
}
