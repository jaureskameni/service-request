package cm.klg.service_request.config;

import cm.klg.common.base.config.TransactionBeansProvider;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_request.adapter.rest.inbound.DefaultDomainToHttpExceptionTranslator;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.application.usecase.CreateNewServiceRequestUseCase;
import cm.klg.service_request.application.usecase.CreateNewUserUseCase;
import cm.klg.service_request.application.usecase.GetAllMyServiceRequestsUseCase;
import cm.klg.service_request.application.usecase.GetAllServiceRequestsByProviderUseCase;
import cm.klg.service_request.application.usecase.GetServiceRequestByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class ServiceRequestBeans implements TransactionBeansProvider {

  @Bean
  public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    return new JwtGrantedAuthoritiesConverter();
  }

  @Bean
  public DomainToHttpExceptionTranslator domainToHttpExceptionTranslator() {
    return new DefaultDomainToHttpExceptionTranslator();
  }

  @Bean
  public CreateNewUserUseCase createNewUserUseCase(UserRepository userRepository) {
    return new CreateNewUserUseCase(userRepository);
  }

  @Bean
  public CreateNewServiceRequestUseCase createNewServiceRequestUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceRequestRepository serviceRequestRepository) {
    return new CreateNewServiceRequestUseCase(serviceProviderRepository, serviceRequestRepository);
  }

  @Bean
  public GetAllMyServiceRequestsUseCase getAllMyServiceRequestsUseCase(
      ServiceRequestRepository serviceRequestRepository) {
    return new GetAllMyServiceRequestsUseCase(serviceRequestRepository);
  }

  @Bean
  public GetAllServiceRequestsByProviderUseCase getAllServiceRequestsByProviderUseCase(
      ServiceRequestRepository serviceRequestRepository) {
    return new GetAllServiceRequestsByProviderUseCase(serviceRequestRepository);
  }

  @Bean
  public GetServiceRequestByIdUseCase getServiceRequestByIdUseCase(
      ServiceRequestRepository serviceRequestRepository) {
    return new GetServiceRequestByIdUseCase(serviceRequestRepository);
  }

  @Bean
  public ApproveServiceProviderUseCase approveServiceProviderUseCase(
      UserRepository userRepository, ServiceProviderRepository serviceProviderRepository) {
    return new ApproveServiceProviderUseCase(userRepository, serviceProviderRepository);
  }
}
