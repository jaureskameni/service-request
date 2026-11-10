package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.event.ServiceRequestCancelledEvent;
import cm.klg.service_request.domain.event.ServiceRequestCreatedEvent;
import cm.klg.service_request.domain.event.ServiceRequestRejectedEvent;
import cm.klg.service_request.domain.review.Review;
import cm.klg.service_request.domain.review.ReviewComment;
import cm.klg.service_request.domain.review.ReviewNotEligibleException;
import cm.klg.service_request.domain.review.ReviewRating;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ServiceRequest {
  private final ServiceRequestId id;
  private final UserId clientId;
  private final ServiceProviderId serviceProviderId;
  private final ServiceTypeId serviceTypeId;
  private final ServiceRequestTitle title;
  private final ServiceRequestDescription description;
  private final ServiceRequestLocation location;
  private ServiceRequestStatus status;
  private ServiceRequestReason reason;
  private UpdatedAt updatedAt;
  private AcceptedAt acceptedAt;
  private final CreatedAt createdAt;
  private ServiceRequestFollowUp followUp;

  private ServiceRequest(
      ServiceRequestId id,
      ServiceRequestParties parties,
      ServiceRequestDetails details,
      ServiceRequestLifecycle lifecycle,
      ServiceRequestFollowUp followUp) {
    this.id = id;
    this.clientId = parties.userId();
    this.serviceProviderId = parties.serviceProviderId();
    this.serviceTypeId = parties.serviceTypeId();
    this.title = details.title();
    this.description = details.description();
    this.location = details.location();
    this.status = lifecycle.status();
    this.updatedAt = lifecycle.updatedAt();
    this.acceptedAt = lifecycle.acceptedAt();
    this.createdAt = lifecycle.createdAt();
    this.reason = lifecycle.reason();
    this.followUp = followUp;
  }

  public static ServiceRequest of(ServiceRequestParties parties, ServiceRequestDetails details) {
    return new ServiceRequest(
        ServiceRequestId.generate(),
        parties,
        details,
        new ServiceRequestLifecycle(
            ServiceRequestStatus.PENDING,
            UpdatedAt.NULL,
            new CreatedAt(LocalDateTime.now()),
            ServiceRequestReason.NULL),
        ServiceRequestFollowUp.NULL);
  }

  public static ServiceRequest reconstitute(
      ServiceRequestId id,
      ServiceRequestParties parties,
      ServiceRequestDetails details,
      ServiceRequestLifecycle lifecycle) {
    return reconstitute(id, parties, details, lifecycle, ServiceRequestFollowUp.NULL);
  }

  public static ServiceRequest reconstitute(
      ServiceRequestId id,
      ServiceRequestParties parties,
      ServiceRequestDetails details,
      ServiceRequestLifecycle lifecycle,
      ServiceRequestFollowUp followUp) {
    return new ServiceRequest(id, parties, details, lifecycle, followUp);
  }

  public boolean isClient(UserId userId) {
    return clientId.equals(userId);
  }

  public boolean isAssignedTo(ServiceProviderId providerId) {
    return serviceProviderId.equals(providerId);
  }

  public ServiceRequestAcceptedEvent accept(ServiceProviderId providerId) {
    if (this.status != ServiceRequestStatus.PENDING) {
      throw new InvalidServiceRequestStatusException();
    }
    if (!Objects.equals(this.serviceProviderId, providerId)) {
      throw new RequestDoesNotBelongToProviderException();
    }
    LocalDateTime acceptedAt = LocalDateTime.now();
    this.status = ServiceRequestStatus.ACCEPTED;
    this.updatedAt = UpdatedAt.from(acceptedAt);
    this.acceptedAt = AcceptedAt.from(acceptedAt);
    this.followUp = ServiceRequestFollowUp.start();

    return this.toServiceRequestAcceptedEvent();
  }

  public ServiceRequestAcceptedEvent toServiceRequestAcceptedEvent() {
    return new ServiceRequestAcceptedEvent(
        this.id, this.clientId, this.serviceProviderId, this.status, this.updatedAt);
  }

  public ServiceRequestCreatedEvent toServiceRequestCreatedEvent() {
    return new ServiceRequestCreatedEvent(
        this.id,
        new ServiceRequestParties(this.clientId, this.serviceProviderId, this.serviceTypeId),
        new ServiceRequestDetails(this.title, this.description, this.location),
        new ServiceRequestLifecycle(
            this.status, this.updatedAt, this.createdAt, this.reason, this.acceptedAt));
  }

  public ServiceRequestRejectedEvent reject(
      ServiceProviderId providerId, ServiceRequestReason reason) {
    if (this.status != ServiceRequestStatus.PENDING) {
      throw new InvalidServiceRequestStatusException();
    }
    if (!Objects.equals(this.serviceProviderId, providerId)) {
      throw new RequestDoesNotBelongToProviderException();
    }
    this.status = ServiceRequestStatus.REJECTED;
    this.reason = reason;
    this.updatedAt = UpdatedAt.from(LocalDateTime.now());

    return this.toServiceRequestRejectedEvent();
  }

  private ServiceRequestRejectedEvent toServiceRequestRejectedEvent() {
    return new ServiceRequestRejectedEvent(
        this.id, this.clientId, this.serviceProviderId, this.status, this.updatedAt, this.reason);
  }

  public ServiceRequestCancelledEvent cancel(UserId userId) {
    if (this.status != ServiceRequestStatus.PENDING) {
      throw new InvalidServiceRequestStatusException();
    }
    if (!Objects.equals(this.clientId, userId)) {
      throw new RequestDoesNotBelongToUserException();
    }
    this.status = ServiceRequestStatus.CANCELLED;
    this.updatedAt = UpdatedAt.from(LocalDateTime.now());

    return this.toServiceRequestCancelledEvent();
  }

  public void answerAgreement(UserId userId, ServiceProviderId providerId, FollowUpAnswer answer) {
    FollowUpParty party = involvedFollowUpParty(userId, providerId);
    existingFollowUp().answerAgreement(party, answer);
    updatedAt = UpdatedAt.from(LocalDateTime.now());
  }

  public FollowUpParty involvedFollowUpParty(UserId userId, ServiceProviderId providerId) {
    if (status != ServiceRequestStatus.ACCEPTED) {
      throw new InvalidServiceRequestStatusException();
    }
    if (this.clientId.equals(userId)) {
      return FollowUpParty.CLIENT;
    }
    if (this.serviceProviderId.equals(providerId)) {
      return FollowUpParty.PROVIDER;
    }
    throw providerId == ServiceProviderId.NULL
        ? new UserIsNotInvolvedException()
        : new ProviderIsNotInvolvedException();
  }

  public ServiceRequestFollowUpActions availableActionsFor(
      UserId userId,
      ServiceProviderId providerId,
      Duration completionDelay,
      boolean reviewAlreadyExists) {
    FollowUpParty party = involvedFollowUpParty(userId, providerId);
    return existingFollowUp().actionsFor(party, completionDelay, reviewAlreadyExists);
  }

  public void answerCompletion(
      UserId userId,
      ServiceProviderId providerId,
      FollowUpAnswer completionAnswer,
      Duration completionDelay) {
    FollowUpParty party = involvedFollowUpParty(userId, providerId);
    followUp.answerCompletion(party, completionAnswer, completionDelay);
    updatedAt = UpdatedAt.from(LocalDateTime.now());
  }

  public Review publishReview(UserId clientId, ReviewRating rating, ReviewComment comment) {
    if (!this.clientId.equals(clientId)) {
      throw new RequestDoesNotBelongToUserException();
    }
    if (!existingFollowUp().isReviewEligible()) {
      throw new ReviewNotEligibleException();
    }
    return Review.of(id, clientId, serviceProviderId, rating, comment);
  }

  private ServiceRequestFollowUp existingFollowUp() {
    if (followUp.isNull()) {
      throw new ServiceRequestFollowUpNotFoundException();
    }
    return followUp;
  }

  private ServiceRequestCancelledEvent toServiceRequestCancelledEvent() {
    return new ServiceRequestCancelledEvent(
        this.id, this.clientId, this.serviceProviderId, this.status, this.updatedAt);
  }
}
