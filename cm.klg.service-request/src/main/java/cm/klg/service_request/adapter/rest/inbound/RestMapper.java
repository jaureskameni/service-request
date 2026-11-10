package cm.klg.service_request.adapter.rest.inbound;

import cm.klg.generated.service.request.adapter.rest.inbound.dto.FollowUpAnswerDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.FollowUpAnswerRequestDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.FollowUpStatusDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ProviderReviewsDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ReviewConfirmationDTO;
import cm.klg.generated.service.request.adapter.rest.inbound.dto.ReviewDTO;
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
import cm.klg.service_request.application.usecase.GetMyServiceRequestsUseCase;
import cm.klg.service_request.application.usecase.GetProviderServiceRequestsUseCase;
import cm.klg.service_request.application.usecase.GetPublishedProviderReviewsUseCase;
import cm.klg.service_request.application.usecase.GetServiceRequestFollowUpUseCase;
import cm.klg.service_request.application.usecase.HideReviewUseCase;
import cm.klg.service_request.application.usecase.PublishServiceRequestReviewUseCase;
import cm.klg.service_request.application.usecase.RejectServiceRequestUseCase;
import cm.klg.service_request.application.views.ReviewSummary;
import cm.klg.service_request.application.views.ReviewViews;
import cm.klg.service_request.application.views.ServiceRequestViews;
import cm.klg.service_request.domain.review.ReviewComment;
import cm.klg.service_request.domain.review.ReviewRating;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.FollowUpAnswer;
import cm.klg.service_request.domain.service_request.FollowUpDateTime;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestFollowUp;
import cm.klg.service_request.domain.service_request.ServiceRequestFollowUpActions;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestReason;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.utils.PaginationFetchRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "userId.value", source = "currentUserId")
  @Mapping(target = "description", source = "serviceRequestRegisterDTO.description")
  @Mapping(target = "title", source = "serviceRequestRegisterDTO.title")
  @Mapping(target = "location", source = "serviceRequestRegisterDTO.location")
  @Mapping(target = "serviceProviderId.value", source = "serviceRequestRegisterDTO.providerId")
  @Mapping(target = "serviceTypeId.value", source = "serviceRequestRegisterDTO.serviceTypeId")
  CreateServiceRequestUseCase.Command toCreateServiceRequestCommand(
      ServiceRequestRegisterDTO serviceRequestRegisterDTO, UUID currentUserId);

  default ServiceRequestDescription toServiceRequestDescription(@Nullable String value) {
    return ServiceRequestDescription.from(value);
  }

  default ServiceRequestTitle toServiceRequestTitle(@Nullable String value) {
    return ServiceRequestTitle.from(value);
  }

  default ServiceRequestLocation toServiceRequestLocation(@Nullable String value) {
    return ServiceRequestLocation.from(value);
  }

  default @Nullable ServiceRequestStatus toServiceRequestStatus(
      @Nullable ServiceRequestStatusDTO status) {
    return status == null ? null : ServiceRequestStatus.valueOf(status.name());
  }

  default GetMyServiceRequestsUseCase.Command toGetMyServiceRequestsCommand(
      Integer limit, @Nullable ServiceRequestStatusDTO status, Integer page, UUID userId) {
    return new GetMyServiceRequestsUseCase.Command(
        UserId.from(userId), toStatus(status), toPaginationFetchRequest(limit, page));
  }

  default GetProviderServiceRequestsUseCase.Command toGetProviderServiceRequestsCommand(
      Integer limit, @Nullable ServiceRequestStatusDTO status, Integer page, UUID userId) {
    return new GetProviderServiceRequestsUseCase.Command(
        UserId.from(userId), toStatus(status), toPaginationFetchRequest(limit, page));
  }

  private static @Nullable ServiceRequestStatus toStatus(@Nullable ServiceRequestStatusDTO status) {
    return status == null ? null : ServiceRequestStatus.valueOf(status.name());
  }

  private static int toLimit(Integer limit) {
    return Optional.ofNullable(limit).orElse(10);
  }

  private static int toPage(Integer page) {
    return Optional.ofNullable(page).orElse(0);
  }

  default PaginationFetchRequest toPaginationFetchRequest(Integer limit, Integer page) {
    return PaginationFetchRequest.of(toLimit(limit), toPage(page));
  }

  default ServiceRequestPaginateDTO toServiceRequestPaginateDTO(
      GetMyServiceRequestsUseCase.Response pageData) {
    return new ServiceRequestPaginateDTO()
        .count(pageData.count())
        .serviceRequest(
            pageData.serviceRequestView1s().stream().map(this::toServiceRequestDTO).toList());
  }

  default ServiceRequestPaginateDTO toServiceRequestPaginateDTO(
      GetProviderServiceRequestsUseCase.Response pageData) {
    return new ServiceRequestPaginateDTO()
        .count(pageData.count())
        .serviceRequest(
            pageData.serviceRequestView1s().stream().map(this::toServiceRequestDTO).toList());
  }

  default ServiceRequestDTO toServiceRequestDTO(
      ServiceRequestViews.ServiceRequestView1 serviceRequestView1) {

    return new ServiceRequestDTO()
        .id(serviceRequestView1.getId())
        .userId(serviceRequestView1.getUserId())
        .providerId(serviceRequestView1.getServiceProviderId())
        .serviceTypeId(serviceRequestView1.getServiceTypeId())
        .status(ServiceRequestStatusDTO.valueOf(serviceRequestView1.getStatus()))
        .title(serviceRequestView1.getTitle())
        .description(serviceRequestView1.getDescription())
        .location(serviceRequestView1.getLocation())
        .reason(serviceRequestView1.getReason())
        .createdAt(serviceRequestView1.getCreatedAt())
        .updatedAt(serviceRequestView1.getUpdatedAt());
  }

  default ServiceRequestDTO toServiceRequestDTO(ServiceRequest serviceRequest) {
    return new ServiceRequestDTO()
        .id(serviceRequest.getId().value())
        .userId(serviceRequest.getClientId().value())
        .providerId(serviceRequest.getServiceProviderId().value())
        .serviceTypeId(serviceRequest.getServiceTypeId().value())
        .status(ServiceRequestStatusDTO.valueOf(serviceRequest.getStatus().name()))
        .title(map(serviceRequest.getTitle()))
        .description(map(serviceRequest.getDescription()))
        .location(map(serviceRequest.getLocation()))
        .reason(map(serviceRequest.getReason()))
        .createdAt(serviceRequest.getCreatedAt().value())
        .updatedAt(serviceRequest.getUpdatedAt().value());
  }

  default @Nullable String map(ServiceRequestTitle title) {
    return title == ServiceRequestTitle.NULL ? null : title.value();
  }

  default @Nullable String map(ServiceRequestDescription description) {
    return description == ServiceRequestDescription.NULL ? null : description.value();
  }

  default @Nullable String map(ServiceRequestLocation location) {
    return location == ServiceRequestLocation.NULL ? null : location.value();
  }

  default @Nullable String map(ServiceRequestReason reason) {
    return reason == ServiceRequestReason.NULL ? null : reason.value();
  }

  default AcceptServiceRequestUseCase.Command toAcceptServiceRequestCommand(
      UUID serviceRequestId, UUID userId) {
    return new AcceptServiceRequestUseCase.Command(
        UserId.from(userId), ServiceRequestId.from(serviceRequestId));
  }

  default RejectServiceRequestUseCase.Command toRejectServiceRequestCommand(
      UUID serviceRequestId, UUID userId, ServiceRequestRejectDTO serviceRequestRejectDTO) {
    return new RejectServiceRequestUseCase.Command(
        UserId.from(userId),
        ServiceRequestId.from(serviceRequestId),
        ServiceRequestReason.from(serviceRequestRejectDTO.getReason()));
  }

  default CancelServiceRequestUseCase.Command toCancelServiceRequestCommand(
      UUID serviceRequestId, UUID userId) {
    return new CancelServiceRequestUseCase.Command(
        UserId.from(userId), ServiceRequestId.from(serviceRequestId));
  }

  default AnswerServiceRequestAgreementUseCase.Command toAgreementCommand(
      UUID serviceRequestId, UUID userId, FollowUpAnswerRequestDTO request) {
    return new AnswerServiceRequestAgreementUseCase.Command(
        ServiceRequestId.from(serviceRequestId), UserId.from(userId), toFollowUpAnswer(request));
  }

  default AnswerServiceRequestCompletionUseCase.Command toCompletionCommand(
      UUID serviceRequestId, UUID userId, FollowUpAnswerRequestDTO request) {
    return new AnswerServiceRequestCompletionUseCase.Command(
        ServiceRequestId.from(serviceRequestId), UserId.from(userId), toFollowUpAnswer(request));
  }

  private static FollowUpAnswer toFollowUpAnswer(FollowUpAnswerRequestDTO request) {
    return FollowUpAnswer.valueOf(request.getAnswer().name());
  }

  default ServiceRequestFollowUpDTO toServiceRequestFollowUpDTO(
      ServiceRequestId serviceRequestId,
      ServiceRequestFollowUp followUp,
      ServiceRequestFollowUpActions actions) {
    return new ServiceRequestFollowUpDTO()
        .serviceRequestId(serviceRequestId.value())
        .status(FollowUpStatusDTO.valueOf(followUp.status().name()))
        .clientAgreement(toFollowUpAnswerDTO(followUp.getClientAgreement()))
        .clientAgreementAt(toLocalDateTime(followUp.getClientAgreementAt()))
        .providerAgreement(toFollowUpAnswerDTO(followUp.getProviderAgreement()))
        .providerAgreementAt(toLocalDateTime(followUp.getProviderAgreementAt()))
        .scheduledAt(toLocalDateTime(followUp.getScheduledAt()))
        .agreementConfirmedAt(toLocalDateTime(followUp.getAgreementConfirmedAt()))
        .clientCompletion(toFollowUpAnswerDTO(followUp.getClientCompletion()))
        .clientCompletionAt(toLocalDateTime(followUp.getClientCompletionAt()))
        .providerCompletion(toFollowUpAnswerDTO(followUp.getProviderCompletion()))
        .providerCompletionAt(toLocalDateTime(followUp.getProviderCompletionAt()))
        .canSubmitReview(actions.canSubmitReview())
        .canAnswerAgreement(actions.canAnswerAgreement())
        .canAnswerCompletion(actions.canAnswerCompletion())
        .completionAvailableAt(toLocalDateTime(actions.completionAvailableAt()));
  }

  default ServiceRequestFollowUpDTO toServiceRequestFollowUpDTO(
      GetServiceRequestFollowUpUseCase.Response response) {
    return toServiceRequestFollowUpDTO(
        response.serviceRequest().getId(),
        response.serviceRequest().getFollowUp(),
        response.actions());
  }

  default PublishServiceRequestReviewUseCase.Command toPublishReviewCommand(
      UUID serviceRequestId, UUID clientId, ReviewRequestDTO request) {
    return new PublishServiceRequestReviewUseCase.Command(
        ServiceRequestId.from(serviceRequestId),
        UserId.from(clientId),
        ReviewRating.from(request.getRating()),
        ReviewComment.from(request.getComment()));
  }

  default HideReviewUseCase.Command toHideReviewCommand(
      UUID reviewId, UUID moderatorId, ReviewModerationRequestDTO request) {
    return new HideReviewUseCase.Command(
        cm.klg.service_request.domain.review.ReviewId.from(reviewId),
        UserId.from(moderatorId),
        request.getReason());
  }

  default ProviderReviewsDTO toProviderReviewsDTO(
      GetPublishedProviderReviewsUseCase.Response response) {
    return new ProviderReviewsDTO()
        .count(response.count())
        .reviews(response.reviews().stream().map(this::toReviewDTO).toList());
  }

  default ReviewDTO toReviewDTO(ReviewViews.PublishedReviewView review) {
    return new ReviewDTO()
        .id(review.getId())
        .serviceRequestId(review.getServiceRequestId())
        .clientId(review.getClientId())
        .rating(review.getRating())
        .comment(review.getComment())
        .confirmation(ReviewConfirmationDTO.valueOf(review.getConfirmation()))
        .createdAt(review.getCreatedAt());
  }

  default ReviewSummaryDTO toReviewSummaryDTO(ReviewSummary summary) {
    return new ReviewSummaryDTO()
        .averageRating(summary.averageRating())
        .reviewCount(summary.reviewCount())
        .oneStarCount(summary.oneStarCount())
        .twoStarCount(summary.twoStarCount())
        .threeStarCount(summary.threeStarCount())
        .fourStarCount(summary.fourStarCount())
        .fiveStarCount(summary.fiveStarCount());
  }

  private static FollowUpAnswerDTO toFollowUpAnswerDTO(FollowUpAnswer answer) {
    return answer == FollowUpAnswer.NULL ? null : FollowUpAnswerDTO.valueOf(answer.name());
  }

  private static @Nullable LocalDateTime toLocalDateTime(FollowUpDateTime dateTime) {
    return dateTime.isNull() ? null : dateTime.valueOrThrow();
  }

  default GetPublishedProviderReviewsUseCase.Command toPublishedProviderReviewsUseCaseCommand(
      UUID providerId, Integer limit, Integer page) {
    return new GetPublishedProviderReviewsUseCase.Command(
        ServiceProviderId.from(providerId), this.toPaginationFetchRequest(limit, page));
  }
}
