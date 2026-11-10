package cm.klg.service_request.config;

import cm.klg.common.base.config.TransactionBeansProvider;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.service_request.adapter.rest.inbound.DefaultDomainToHttpExceptionTranslator;
import cm.klg.service_request.application.config.Configurations;
import cm.klg.service_request.application.outbound.DomainEventPublisher;
import cm.klg.service_request.application.outbound.ReviewRepository;
import cm.klg.service_request.application.outbound.ServiceProviderRepository;
import cm.klg.service_request.application.outbound.ServiceRequestRepository;
import cm.klg.service_request.application.outbound.UserRepository;
import cm.klg.service_request.application.usecase.AcceptServiceRequestUseCase;
import cm.klg.service_request.application.usecase.AnswerServiceRequestAgreementUseCase;
import cm.klg.service_request.application.usecase.AnswerServiceRequestCompletionUseCase;
import cm.klg.service_request.application.usecase.ApproveServiceProviderUseCase;
import cm.klg.service_request.application.usecase.CancelServiceRequestUseCase;
import cm.klg.service_request.application.usecase.CreateServiceRequestUseCase;
import cm.klg.service_request.application.usecase.CreateUserUseCase;
import cm.klg.service_request.application.usecase.DeleteUserUseCase;
import cm.klg.service_request.application.usecase.GetMyProviderReviewSummaryUseCase;
import cm.klg.service_request.application.usecase.GetMyServiceRequestsUseCase;
import cm.klg.service_request.application.usecase.GetProviderServiceRequestsUseCase;
import cm.klg.service_request.application.usecase.GetPublishedProviderReviewSummaryUseCase;
import cm.klg.service_request.application.usecase.GetPublishedProviderReviewsUseCase;
import cm.klg.service_request.application.usecase.GetServiceRequestByIdUseCase;
import cm.klg.service_request.application.usecase.GetServiceRequestFollowUpUseCase;
import cm.klg.service_request.application.usecase.HideReviewUseCase;
import cm.klg.service_request.application.usecase.PublishServiceRequestReviewUseCase;
import cm.klg.service_request.application.usecase.RejectServiceRequestUseCase;
import cm.klg.service_request.application.usecase.UpdateUserUseCase;
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
  public CreateUserUseCase createUserUseCase(UserRepository userRepository) {
    return new CreateUserUseCase(userRepository);
  }

  @Bean
  public UpdateUserUseCase updateUserUseCase(UserRepository userRepository) {
    return new UpdateUserUseCase(userRepository);
  }

  @Bean
  public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
    return new DeleteUserUseCase(userRepository);
  }

  @Bean
  public CreateServiceRequestUseCase createServiceRequestUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceRequestRepository serviceRequestRepository,
      DomainEventPublisher domainEventPublisher) {
    return new CreateServiceRequestUseCase(
        serviceProviderRepository, serviceRequestRepository, domainEventPublisher);
  }

  @Bean
  public GetMyServiceRequestsUseCase getMyServiceRequestsUseCase(
      ServiceRequestRepository serviceRequestRepository) {
    return new GetMyServiceRequestsUseCase(serviceRequestRepository);
  }

  @Bean
  public GetProviderServiceRequestsUseCase getProviderServiceRequestsUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceRequestRepository serviceRequestRepository) {
    return new GetProviderServiceRequestsUseCase(
        serviceProviderRepository, serviceRequestRepository);
  }

  @Bean
  public GetServiceRequestByIdUseCase getServiceRequestByIdUseCase(
      ServiceRequestRepository serviceRequestRepository,
      ServiceProviderRepository serviceProviderRepository) {
    return new GetServiceRequestByIdUseCase(serviceRequestRepository, serviceProviderRepository);
  }

  @Bean
  public AcceptServiceRequestUseCase acceptServiceRequestUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceRequestRepository serviceRequestRepository,
      DomainEventPublisher domainEventPublisher) {
    return new AcceptServiceRequestUseCase(
        serviceProviderRepository, serviceRequestRepository, domainEventPublisher);
  }

  @Bean
  public GetServiceRequestFollowUpUseCase getServiceRequestFollowUpUseCase(
      ServiceRequestRepository serviceRequestRepository,
      ServiceProviderRepository serviceProviderRepository,
      ReviewRepository reviewRepository,
      Configurations configurations) {
    return new GetServiceRequestFollowUpUseCase(
        serviceRequestRepository, serviceProviderRepository, reviewRepository, configurations);
  }

  @Bean
  public AnswerServiceRequestAgreementUseCase answerServiceRequestAgreementUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceRequestRepository serviceRequestRepository) {
    return new AnswerServiceRequestAgreementUseCase(
        serviceRequestRepository, serviceProviderRepository);
  }

  @Bean
  public AnswerServiceRequestCompletionUseCase answerServiceRequestCompletionUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceRequestRepository serviceRequestRepository,
      Configurations configurations) {
    return new AnswerServiceRequestCompletionUseCase(
        serviceRequestRepository, serviceProviderRepository, configurations);
  }

  @Bean
  public PublishServiceRequestReviewUseCase publishServiceRequestReviewUseCase(
      ServiceRequestRepository serviceRequestRepository, ReviewRepository reviewRepository) {
    return new PublishServiceRequestReviewUseCase(serviceRequestRepository, reviewRepository);
  }

  @Bean
  public GetPublishedProviderReviewsUseCase getPublishedProviderReviewsUseCase(
      ReviewRepository reviewRepository, ServiceProviderRepository serviceProviderRepository) {
    return new GetPublishedProviderReviewsUseCase(reviewRepository, serviceProviderRepository);
  }

  @Bean
  public GetPublishedProviderReviewSummaryUseCase getPublishedProviderReviewSummaryUseCase(
      ReviewRepository reviewRepository, ServiceProviderRepository serviceProviderRepository) {
    return new GetPublishedProviderReviewSummaryUseCase(
        reviewRepository, serviceProviderRepository);
  }

  @Bean
  public GetMyProviderReviewSummaryUseCase getMyProviderReviewSummaryUseCase(
      ReviewRepository reviewRepository, ServiceProviderRepository serviceProviderRepository) {
    return new GetMyProviderReviewSummaryUseCase(reviewRepository, serviceProviderRepository);
  }

  @Bean
  public HideReviewUseCase hideReviewUseCase(ReviewRepository reviewRepository) {
    return new HideReviewUseCase(reviewRepository);
  }

  @Bean
  public RejectServiceRequestUseCase rejectServiceRequestUseCase(
      ServiceProviderRepository serviceProviderRepository,
      ServiceRequestRepository serviceRequestRepository,
      DomainEventPublisher domainEventPublisher) {
    return new RejectServiceRequestUseCase(
        serviceProviderRepository, serviceRequestRepository, domainEventPublisher);
  }

  @Bean
  public CancelServiceRequestUseCase cancelServiceRequestUseCase(
      ServiceRequestRepository serviceRequestRepository,
      DomainEventPublisher domainEventPublisher) {
    return new CancelServiceRequestUseCase(serviceRequestRepository, domainEventPublisher);
  }

  @Bean
  public ApproveServiceProviderUseCase approveServiceProviderUseCase(
      UserRepository userRepository, ServiceProviderRepository serviceProviderRepository) {
    return new ApproveServiceProviderUseCase(userRepository, serviceProviderRepository);
  }
}
