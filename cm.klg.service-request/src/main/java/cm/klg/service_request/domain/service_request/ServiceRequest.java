package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.UpdatedAt;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.event.ServiceRequestCancelledEvent;
import cm.klg.service_request.domain.event.ServiceRequestCreatedEvent;
import cm.klg.service_request.domain.event.ServiceRequestRejectedEvent;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.IdentityId;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceRequest {
  private final ServiceRequestId id;
  private final IdentityId userId;
  private final ServiceProviderId serviceProviderId;
  private final ServiceTypeId serviceTypeId;
  @Nullable private final ServiceRequestTitle title;
  @Nullable private final ServiceRequestDescription description;
  @Nullable private final ServiceRequestLocation location;
  private ServiceRequestStatus status;
  @Nullable private ServiceRequestReason reason;
  @Nullable private UpdatedAt updatedAt;
  private final CreatedAt createdAt;

  private ServiceRequest(
      ServiceRequestId id,
      ServiceRequestParties parties,
      ServiceRequestDetails details,
      ServiceRequestLifecycle lifecycle) {
    this.id = id;
    this.userId = parties.userId();
    this.serviceProviderId = parties.serviceProviderId();
    this.serviceTypeId = parties.serviceTypeId();
    this.title = details.title();
    this.description = details.description();
    this.location = details.location();
    this.status = lifecycle.status();
    this.updatedAt = lifecycle.updatedAt();
    this.createdAt = lifecycle.createdAt();
    this.reason = lifecycle.reason();
  }

  public static ServiceRequest of(ServiceRequestParties parties, ServiceRequestDetails details) {
    return new ServiceRequest(
        ServiceRequestId.generate(),
        parties,
        details,
        new ServiceRequestLifecycle(
            ServiceRequestStatus.PENDING, null, new CreatedAt(LocalDateTime.now()), null));
  }

  public static ServiceRequest reconstitute(
      ServiceRequestId id,
      ServiceRequestParties parties,
      ServiceRequestDetails details,
      ServiceRequestLifecycle lifecycle) {
    return new ServiceRequest(id, parties, details, lifecycle);
  }

  public ServiceRequestAcceptedEvent accept(ServiceProviderId providerId) {
    if (this.status != ServiceRequestStatus.PENDING) {
      throw new InvalidServiceRequestStatusException();
    }
    if (!Objects.equals(this.serviceProviderId, providerId)) {
      throw new RequestDoesNotBelongToProviderException();
    }
    this.status = ServiceRequestStatus.ACCEPTED;
    this.updatedAt = UpdatedAt.from(LocalDateTime.now());

    return this.toServiceRequestAcceptedEvent();
  }

  public ServiceRequestAcceptedEvent toServiceRequestAcceptedEvent() {
    return new ServiceRequestAcceptedEvent(
        this.id,
        this.userId,
        this.serviceProviderId,
        this.status,
        Objects.requireNonNull(this.updatedAt));
  }

  public ServiceRequestCreatedEvent toServiceRequestCreatedEvent() {
    return new ServiceRequestCreatedEvent(
        this.id,
        new ServiceRequestParties(this.userId, this.serviceProviderId, this.serviceTypeId),
        new ServiceRequestDetails(this.title, this.description, this.location),
        new ServiceRequestLifecycle(this.status, this.updatedAt, this.createdAt, this.reason));
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
        this.id,
        this.userId,
        this.serviceProviderId,
        this.status,
        Objects.requireNonNull(this.updatedAt),
        this.reason);
  }

  public ServiceRequestCancelledEvent cancel(IdentityId userId) {
    if (this.status != ServiceRequestStatus.PENDING) {
      throw new InvalidServiceRequestStatusException();
    }
    if (!Objects.equals(this.userId, userId)) {
      throw new RequestDoesNotBelongToUserException();
    }
    this.status = ServiceRequestStatus.CANCELLED;
    this.updatedAt = UpdatedAt.from(LocalDateTime.now());

    return this.toServiceRequestCancelledEvent();
  }

  private ServiceRequestCancelledEvent toServiceRequestCancelledEvent() {
    return new ServiceRequestCancelledEvent(
        this.id,
        this.userId,
        this.serviceProviderId,
        this.status,
        Objects.requireNonNull(this.updatedAt));
  }
}
