package cm.klg.service_request.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_request.adapter.rest.inbound.DefaultDomainToHttpExceptionTranslator;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.application.usecase.CreateNewUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

class ServiceRequestBeansTest {

  private final ServiceRequestBeans beans = new ServiceRequestBeans();

  @Test
  void shouldCreateJwtGrantedAuthoritiesConverter() {
    assertThat(beans.jwtGrantedAuthoritiesConverter())
        .isInstanceOf(JwtGrantedAuthoritiesConverter.class);
  }

  @Test
  void shouldCreateDomainToHttpExceptionTranslator() {
    DomainToHttpExceptionTranslator translator = beans.domainToHttpExceptionTranslator();

    assertThat(translator).isInstanceOf(DefaultDomainToHttpExceptionTranslator.class);
  }

  @Test
  void shouldCreateCreateNewUserUseCase() {
    UserRepository userRepository = mock(UserRepository.class);

    CreateNewUserUseCase useCase = beans.createNewUserUseCase(userRepository);

    assertThat(useCase.userRepository()).isEqualTo(userRepository);
  }

  @Test
  void shouldCreateApproveServiceProviderUseCase() {
    UserRepository userRepository = mock(UserRepository.class);
    ServiceProviderRepository serviceProviderRepository = mock(ServiceProviderRepository.class);

    ApproveServiceProviderUseCase useCase =
        beans.approveServiceProviderUseCase(userRepository, serviceProviderRepository);

    assertThat(useCase.userRepository()).isEqualTo(userRepository);
    assertThat(useCase.serviceProviderRepository()).isEqualTo(serviceProviderRepository);
  }
}
