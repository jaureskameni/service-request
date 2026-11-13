package cm.klg.service_request.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.common.base.entity.PhoneNumberJpa;
import cm.klg.service_request.application.views.ReviewRatingSummary;
import cm.klg.service_request.application.views.ReviewSummary;
import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.review.Content;
import cm.klg.service_request.domain.review.Lifecycle;
import cm.klg.service_request.domain.review.Moderation;
import cm.klg.service_request.domain.review.Parties;
import cm.klg.service_request.domain.review.Review;
import cm.klg.service_request.domain.review.ReviewComment;
import cm.klg.service_request.domain.review.ReviewId;
import cm.klg.service_request.domain.review.ReviewRating;
import cm.klg.service_request.domain.review.ReviewStatus;
import cm.klg.service_request.domain.service_provider.ServiceProvider;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_provider.ServiceProviderStatus;
import cm.klg.service_request.domain.service_request.AcceptedAt;
import cm.klg.service_request.domain.service_request.FollowUpAnswer;
import cm.klg.service_request.domain.service_request.FollowUpDateTime;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestDescription;
import cm.klg.service_request.domain.service_request.ServiceRequestDetails;
import cm.klg.service_request.domain.service_request.ServiceRequestFollowUp;
import cm.klg.service_request.domain.service_request.ServiceRequestFollowUpId;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestLifecycle;
import cm.klg.service_request.domain.service_request.ServiceRequestLocation;
import cm.klg.service_request.domain.service_request.ServiceRequestParties;
import cm.klg.service_request.domain.service_request.ServiceRequestReason;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.service_request.ServiceRequestTitle;
import cm.klg.service_request.domain.service_request.ServiceTypeId;
import cm.klg.service_request.domain.user.EmailAddress;
import cm.klg.service_request.domain.user.Firstname;
import cm.klg.service_request.domain.user.Lastname;
import cm.klg.service_request.domain.user.PhoneNumber;
import cm.klg.service_request.domain.user.User;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.domain.user.UserProfile;
import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JpaMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "serviceProvider", source = "serviceProvider")
  @Mapping(target = "createdAt", source = "createdAt.value")
  UserJpa toUserJpa(User user);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "emailAddress", source = "email.value")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "serviceProvider", source = "serviceProvider")
  @Mapping(target = "createdAt", source = "createdAt.value")
  void updateUserJpa(@MappingTarget UserJpa target, User user);

  default User toReviewSummary(UserJpa userJpa) {
    PhoneNumber phoneNumber =
        new PhoneNumber(
            userJpa.getPhoneNumber().getCountryCode(), userJpa.getPhoneNumber().getNumber());
    UserProfile userProfile =
        new UserProfile(
            Firstname.from(userJpa.getFirstname()),
            Lastname.from(userJpa.getLastname()),
            EmailAddress.from(userJpa.getEmailAddress()),
            phoneNumber);
    return User.reconstitute(
        UserId.from(userJpa.getId()),
        userProfile,
        userJpa.isServiceProvider(),
        CreatedAt.from(userJpa.getCreatedAt()));
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "approvedAt", source = "approvedAt.value")
  ServiceProviderJpa toJpa(ServiceProvider serviceProvider);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userId", source = "userId.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "approvedAt", source = "approvedAt.value")
  void fromServiceProvider(
      @MappingTarget ServiceProviderJpa serviceProviderJpa, ServiceProvider serviceProvider);

  default PhoneNumberJpa toJpa(PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return null;
    }
    return new PhoneNumberJpa(phoneNumber.countryCode(), phoneNumber.number());
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "clientId", source = "clientId.value")
  @Mapping(target = "serviceTypeId", source = "serviceTypeId.value")
  @Mapping(target = "providerId", source = "serviceProviderId.value")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "location", source = "location")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  @Mapping(target = "acceptedAt", source = "acceptedAt.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "reason", source = "reason")
  ServiceRequestJpa toServiceRequestJpaFields(@NonNull ServiceRequest serviceRequest);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "clientId", source = "clientId.value")
  @Mapping(target = "serviceTypeId", source = "serviceTypeId.value")
  @Mapping(target = "providerId", source = "serviceProviderId.value")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "location", source = "location")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "updatedAt", source = "updatedAt.value")
  @Mapping(target = "acceptedAt", source = "acceptedAt.value")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "reason", source = "reason")
  void updateServiceRequestJpaFields(
      @MappingTarget ServiceRequestJpa serviceRequestJpa, ServiceRequest serviceRequest);

  default ServiceRequestJpa toJpa(@NonNull ServiceRequest serviceRequest) {
    ServiceRequestJpa jpa = toServiceRequestJpaFields(serviceRequest);
    if (!serviceRequest.getFollowUp().isNull()) {
      jpa.setFollowUp(toFollowUpJpa(serviceRequest.getFollowUp(), jpa));
    }
    return jpa;
  }

  default void toJpa(@MappingTarget ServiceRequestJpa target, ServiceRequest serviceRequest) {
    updateServiceRequestJpaFields(target, serviceRequest);
    if (serviceRequest.getFollowUp().isNull()) {
      target.setFollowUp(null);
    } else if (target.getFollowUp() == null) {
      target.setFollowUp(toFollowUpJpa(serviceRequest.getFollowUp(), target));
    } else {
      updateFollowUpJpa(target.getFollowUp(), serviceRequest.getFollowUp());
    }
  }

  default @Nullable String toJpaValue(ServiceRequestTitle title) {
    return title == ServiceRequestTitle.NULL ? null : title.value();
  }

  default @Nullable String toJpaValue(ServiceRequestDescription description) {
    return description == ServiceRequestDescription.NULL ? null : description.value();
  }

  default @Nullable String toJpaValue(ServiceRequestLocation location) {
    return location == ServiceRequestLocation.NULL ? null : location.value();
  }

  default @Nullable String toJpaValue(ServiceRequestReason reason) {
    return reason == ServiceRequestReason.NULL ? null : reason.value();
  }

  default ServiceProvider toReviewSummary(ServiceProviderJpa jpa) {
    return ServiceProvider.reconstitute(
        ServiceProviderId.from(jpa.getId()),
        UserId.from(jpa.getUserId()),
        ServiceProviderStatus.valueOf(jpa.getStatus()),
        CreatedAt.from(jpa.getApprovedAt()));
  }

  default ServiceRequest toReviewSummary(ServiceRequestJpa jpa) {
    return ServiceRequest.reconstitute(
        ServiceRequestId.from(jpa.getId()),
        new ServiceRequestParties(
            UserId.from(jpa.getClientId()),
            ServiceProviderId.from(jpa.getProviderId()),
            ServiceTypeId.from(jpa.getServiceTypeId())),
        new ServiceRequestDetails(
            ServiceRequestTitle.from(jpa.getTitle()),
            ServiceRequestDescription.from(jpa.getDescription()),
            ServiceRequestLocation.from(jpa.getLocation())),
        new ServiceRequestLifecycle(
            ServiceRequestStatus.valueOf(jpa.getStatus()),
            UpdatedAt.from(jpa.getUpdatedAt()),
            CreatedAt.from(jpa.getCreatedAt()),
            ServiceRequestReason.from(jpa.getReason()),
            AcceptedAt.from(jpa.getAcceptedAt())),
        jpa.getFollowUp() == null
            ? ServiceRequestFollowUp.NULL
            : toReviewSummary(jpa.getFollowUp()));
  }

  default ReviewSummary toReviewSummary(ReviewRatingSummary summary) {
    return new ReviewSummary(
        valueOrZero(summary.getAverageRating()),
        valueOrZero(summary.getReviewCount()),
        valueOrZero(summary.getOneStarCount()),
        valueOrZero(summary.getTwoStarCount()),
        valueOrZero(summary.getThreeStarCount()),
        valueOrZero(summary.getFourStarCount()),
        valueOrZero(summary.getFiveStarCount()));
  }

  private static long valueOrZero(@Nullable Long value) {
    return value == null ? 0 : value;
  }

  private static double valueOrZero(@Nullable Double value) {
    return value == null ? 0.0 : value;
  }

  default ServiceRequestFollowUpJpa toFollowUpJpa(
      ServiceRequestFollowUp followUp, ServiceRequestJpa serviceRequest) {
    var jpa = new ServiceRequestFollowUpJpa();
    jpa.setId(followUp.getId().value());
    jpa.setServiceRequest(serviceRequest);
    mapFollowUpFields(jpa, followUp);
    return jpa;
  }

  private void mapFollowUpFields(ServiceRequestFollowUpJpa jpa, ServiceRequestFollowUp followUp) {
    jpa.setClientAgreement(toJpaValue(followUp.getClientAgreement()));
    jpa.setClientAgreementAt(toJpaValue(followUp.getClientAgreementAt()));
    jpa.setProviderAgreement(toJpaValue(followUp.getProviderAgreement()));
    jpa.setProviderAgreementAt(toJpaValue(followUp.getProviderAgreementAt()));
    jpa.setScheduledAt(toJpaValue(followUp.getScheduledAt()));
    jpa.setAgreementConfirmedAt(toJpaValue(followUp.getAgreementConfirmedAt()));
    jpa.setClientCompletion(toJpaValue(followUp.getClientCompletion()));
    jpa.setClientCompletionAt(toJpaValue(followUp.getClientCompletionAt()));
    jpa.setProviderCompletion(toJpaValue(followUp.getProviderCompletion()));
    jpa.setProviderCompletionAt(toJpaValue(followUp.getProviderCompletionAt()));
  }

  default ServiceRequestFollowUp toReviewSummary(ServiceRequestFollowUpJpa jpa) {
    return ServiceRequestFollowUp.reconstitute(
        ServiceRequestFollowUpId.from(jpa.getId()),
        fromJpaAnswer(jpa.getClientAgreement()),
        FollowUpDateTime.from(jpa.getClientAgreementAt()),
        fromJpaAnswer(jpa.getProviderAgreement()),
        FollowUpDateTime.from(jpa.getProviderAgreementAt()),
        FollowUpDateTime.from(jpa.getScheduledAt()),
        FollowUpDateTime.from(jpa.getAgreementConfirmedAt()),
        fromJpaAnswer(jpa.getClientCompletion()),
        FollowUpDateTime.from(jpa.getClientCompletionAt()),
        fromJpaAnswer(jpa.getProviderCompletion()),
        FollowUpDateTime.from(jpa.getProviderCompletionAt()));
  }

  default void updateFollowUpJpa(
      @MappingTarget ServiceRequestFollowUpJpa target, ServiceRequestFollowUp followUp) {
    mapFollowUpFields(target, followUp);
  }

  default ReviewJpa toReviewJpa(Review review) {
    var jpa = new ReviewJpa();
    updateReviewJpa(jpa, review);
    return jpa;
  }

  default void updateReviewJpa(@MappingTarget ReviewJpa jpa, Review review) {
    jpa.setId(review.getId().value());
    jpa.setServiceRequestId(review.getServiceRequestId().value());
    jpa.setClientId(review.getClientId().value());
    jpa.setProviderId(review.getProviderId().value());
    jpa.setRating(review.getRating().value());
    jpa.setComment(toJpaValue(review.getComment()));
    jpa.setStatus(review.getStatus().name());
    jpa.setCreatedAt(review.getCreatedAt().value());
    jpa.setModerationReason(toJpaValue(review.getModerationReason()));
    jpa.setModeratedBy(
        review.getModeratedBy() == UserId.NULL ? null : review.getModeratedBy().value());
    jpa.setModeratedAt(review.getModeratedAt().value());
  }

  default Review toReviewSummary(ReviewJpa jpa) {
    return new Review(
        ReviewId.from(jpa.getId()),
        new Parties(
            ServiceRequestId.from(jpa.getServiceRequestId()),
            UserId.from(jpa.getClientId()),
            ServiceProviderId.from(jpa.getProviderId())),
        new Content(ReviewRating.from(jpa.getRating()), ReviewComment.from(jpa.getComment())),
        new Lifecycle(ReviewStatus.valueOf(jpa.getStatus()), CreatedAt.from(jpa.getCreatedAt())),
        new Moderation(
            ReviewComment.from(jpa.getModerationReason()),
            jpa.getModeratedBy() == null ? UserId.NULL : UserId.from(jpa.getModeratedBy()),
            UpdatedAt.from(jpa.getModeratedAt())));
  }

  default @Nullable String toJpaValue(FollowUpAnswer answer) {
    return answer == FollowUpAnswer.NULL ? null : answer.name();
  }

  default FollowUpAnswer fromJpaAnswer(@Nullable String answer) {
    return answer == null ? FollowUpAnswer.NULL : FollowUpAnswer.valueOf(answer);
  }

  default @Nullable LocalDateTime toJpaValue(FollowUpDateTime dateTime) {
    return dateTime.isNull() ? null : dateTime.valueOrThrow();
  }

  default @Nullable String toJpaValue(ReviewComment comment) {
    return comment == ReviewComment.NULL ? null : comment.value();
  }
}
