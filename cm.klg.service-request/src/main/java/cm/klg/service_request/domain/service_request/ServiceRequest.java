package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceRequest {
  private final ServiceRequestId id;
  private final UserId userId;
  private final ServiceProviderId serviceProviderId;
  private final ServiceTypeId serviceTypeId;
  @Nullable private final ServiceRequestTitle title;
  @Nullable private final ServiceRequestDescription description;
  @Nullable private final ServiceRequestLocation location;
  private ServiceRequestStatus status;
  @Nullable private CreatedAt acceptAt;
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
    this.acceptAt = lifecycle.acceptAt();
    this.createdAt = lifecycle.createdAt();
  }

  public static ServiceRequest of(ServiceRequestParties parties, ServiceRequestDetails details) {
    return new ServiceRequest(
        ServiceRequestId.generate(),
        parties,
        details,
        new ServiceRequestLifecycle(
            ServiceRequestStatus.PENDING, null, new CreatedAt(LocalDateTime.now())));
  }

  public static ServiceRequest reconstitute(
      ServiceRequestId id,
      ServiceRequestParties parties,
      ServiceRequestDetails details,
      ServiceRequestLifecycle lifecycle) {
    return new ServiceRequest(id, parties, details, lifecycle);
  }

  public ServiceRequestAcceptedEvent accept(ServiceProviderId providerId) {
    if (!Objects.equals(this.serviceProviderId, providerId)) {
      throw new RequestDoesNotBelongToProviderException();
    }
    this.status = ServiceRequestStatus.ACCEPTED;
    this.acceptAt = new CreatedAt(LocalDateTime.now());

    return this.toServiceRequestAcceptedEvent();
  }

  public ServiceRequestAcceptedEvent toServiceRequestAcceptedEvent() {
    return new ServiceRequestAcceptedEvent(
        this.id,
        new ServiceRequestParties(this.userId, this.serviceProviderId, this.serviceTypeId),
        new ServiceRequestDetails(this.title, this.description, this.location),
        new ServiceRequestLifecycle(this.status, this.acceptAt, this.createdAt));
  }
}
