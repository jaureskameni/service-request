package cm.klg.service_request.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.service.request.adapter.rest.inbound.api.ReviewApi;
import cm.klg.generated.service.request.adapter.rest.inbound.api.ReviewModerationApi;
import cm.klg.generated.service.request.adapter.rest.inbound.api.ServiceRequestApi;
import cm.klg.generated.service.request.adapter.rest.inbound.api.ServiceRequestFollowUpApi;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.CreationResponseDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.FollowUpAnswerRequestDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ProviderReviewsDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ReviewModerationRequestDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ReviewRequestDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ReviewSummaryDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestFollowUpDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestPaginateDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRegisterDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestRejectDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ServiceRequestStatusDTO;
import cm.klg.service_request.application.usecase.AcceptServiceRequestUseCase;
import cm.klg.service_request.application.usecase.AnswerServiceRequestAgreementUseCase;
import cm.klg.service_request.application.usecase.AnswerServiceRequestCompletionUseCase;
import cm.klg.service_request.application.usecase.CancelServiceRequestUseCase;
import cm.klg.service_request.application.usecase.CreateServiceRequestUseCase;
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
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.user.UserId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServiceRequestController
    implements ServiceRequestApi,
        ServiceRequestFollowUpApi,
        ReviewApi,
        ReviewModerationApi,
        WithAuthenticationSupport {
  private final UseCaseExecutor useCaseExecutor;
  private final RestMapper restMapper;
  private final CreateServiceRequestUseCase createServiceRequestUseCase;
  private final GetMyServiceRequestsUseCase getMyServiceRequestsUseCase;
  private final GetProviderServiceRequestsUseCase getProviderServiceRequestsUseCase;
  private final GetServiceRequestByIdUseCase getServiceRequestByIdUseCase;
  private final AcceptServiceRequestUseCase acceptServiceRequestUseCase;
  private final RejectServiceRequestUseCase rejectServiceRequestUseCase;
  private final CancelServiceRequestUseCase cancelServiceRequestUseCase;
  private final GetServiceRequestFollowUpUseCase getServiceRequestFollowUpUseCase;
  private final AnswerServiceRequestAgreementUseCase answerServiceRequestAgreementUseCase;
  private final AnswerServiceRequestCompletionUseCase answerServiceRequestCompletionUseCase;
  private final PublishServiceRequestReviewUseCase publishServiceRequestReviewUseCase;
  private final GetPublishedProviderReviewsUseCase getPublishedProviderReviewsUseCase;
  private final GetPublishedProviderReviewSummaryUseCase getPublishedProviderReviewSummaryUseCase;
  private final GetMyProviderReviewSummaryUseCase getMyProviderReviewSummaryUseCase;
  private final HideReviewUseCase hideReviewUseCase;

  @Override
  public ResponseEntity<Void> publishServiceRequestReview(
      UUID serviceRequestId, ReviewRequestDTO reviewRequestDTO) {
    useCaseExecutor.runCommand(
        () ->
            publishServiceRequestReviewUseCase.execute(
                restMapper.toPublishReviewCommand(
                    serviceRequestId, getCurrentUserId(), reviewRequestDTO)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ProviderReviewsDTO> getProviderReviews(
      UUID providerId, Integer limit, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getPublishedProviderReviewsUseCase.getPublishedReviews(
                    restMapper.toPublishedProviderReviewsUseCaseCommand(providerId, limit, page)));
    return ResponseEntity.ok(restMapper.toProviderReviewsDTO(result));
  }

  @Override
  public ResponseEntity<ReviewSummaryDTO> getProviderReviewSummary(UUID providerId) {
    var summary =
        useCaseExecutor.executeQuery(
            () ->
                getPublishedProviderReviewSummaryUseCase.execute(
                    ServiceProviderId.from(providerId)));
    return ResponseEntity.ok(restMapper.toReviewSummaryDTO(summary));
  }

  @Override
  public ResponseEntity<ReviewSummaryDTO> getMyReviewSummary() {
    var summary =
        useCaseExecutor.executeQuery(
            () -> getMyProviderReviewSummaryUseCase.execute(UserId.from(getCurrentUserId())));
    return ResponseEntity.ok(restMapper.toReviewSummaryDTO(summary));
  }

  @Override
  public ResponseEntity<Void> hideReview(
      UUID reviewId, ReviewModerationRequestDTO reviewModerationRequestDTO) {
    useCaseExecutor.runCommand(
        () ->
            hideReviewUseCase.execute(
                restMapper.toHideReviewCommand(
                    reviewId, getCurrentUserId(), reviewModerationRequestDTO)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ServiceRequestFollowUpDTO> getServiceRequestFollowUp(
      UUID serviceRequestId) {
    var followUpResponse =
        useCaseExecutor.executeQuery(
            () ->
                getServiceRequestFollowUpUseCase.execute(
                    new GetServiceRequestFollowUpUseCase.Command(
                        ServiceRequestId.from(serviceRequestId), UserId.from(getCurrentUserId()))));
    return ResponseEntity.ok(restMapper.toServiceRequestFollowUpDTO(followUpResponse));
  }

  @Override
  public ResponseEntity<Void> answerServiceRequestAgreement(
      UUID serviceRequestId, FollowUpAnswerRequestDTO request) {
    useCaseExecutor.runCommand(
        () ->
            answerServiceRequestAgreementUseCase.execute(
                restMapper.toAgreementCommand(serviceRequestId, getCurrentUserId(), request)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> answerServiceRequestCompletion(
      UUID serviceRequestId, FollowUpAnswerRequestDTO request) {
    useCaseExecutor.runCommand(
        () ->
            answerServiceRequestCompletionUseCase.execute(
                restMapper.toCompletionCommand(serviceRequestId, getCurrentUserId(), request)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ServiceRequestDTO> getServiceRequestById(UUID serviceRequestId) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getServiceRequestByIdUseCase.execute(
                    new GetServiceRequestByIdUseCase.Command(
                        new ServiceRequestId(serviceRequestId), UserId.from(getCurrentUserId()))));

    return ResponseEntity.ok(restMapper.toServiceRequestDTO(result));
  }

  @Override
  public ResponseEntity<Void> acceptServiceRequest(UUID serviceRequestId) {
    useCaseExecutor.runCommand(
        () ->
            acceptServiceRequestUseCase.execute(
                restMapper.toAcceptServiceRequestCommand(serviceRequestId, getCurrentUserId())));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> rejectServiceRequest(
      UUID serviceRequestId, ServiceRequestRejectDTO serviceRequestRejectDTO) {
    useCaseExecutor.runCommand(
        () ->
            rejectServiceRequestUseCase.execute(
                restMapper.toRejectServiceRequestCommand(
                    serviceRequestId, getCurrentUserId(), serviceRequestRejectDTO)));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> cancelServiceRequest(UUID serviceRequestId) {
    useCaseExecutor.runCommand(
        () ->
            cancelServiceRequestUseCase.execute(
                restMapper.toCancelServiceRequestCommand(serviceRequestId, getCurrentUserId())));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CreationResponseDTO> createNewServiceRequest(
      ServiceRequestRegisterDTO serviceRequestRegisterDTO) {
    ServiceRequestId result =
        useCaseExecutor.executeCommand(
            () ->
                createServiceRequestUseCase.execute(
                    restMapper.toCreateServiceRequestCommand(
                        serviceRequestRegisterDTO, getCurrentUserId())));
    return ResponseEntity.status(CREATED).body(new CreationResponseDTO().newId(result.value()));
  }

  @Override
  public ResponseEntity<ServiceRequestPaginateDTO> getAllMyServiceRequest(
      Integer limit, ServiceRequestStatusDTO status, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getMyServiceRequestsUseCase.execute(
                    restMapper.toGetMyServiceRequestsCommand(
                        limit, status, page, getCurrentUserId())));
    return ResponseEntity.status(OK).body(restMapper.toServiceRequestPaginateDTO(result));
  }

  @Override
  public ResponseEntity<ServiceRequestPaginateDTO> getAllServiceRequestsForCurrentProvider(
      Integer limit, ServiceRequestStatusDTO status, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getProviderServiceRequestsUseCase.execute(
                    restMapper.toGetProviderServiceRequestsCommand(
                        limit, status, page, getCurrentUserId())));
    return ResponseEntity.status(OK).body(restMapper.toServiceRequestPaginateDTO(result));
  }

  private UUID getCurrentUserId() {
    return getCurrentUser().getId().map(UUID::fromString).orElseThrow();
  }
}
