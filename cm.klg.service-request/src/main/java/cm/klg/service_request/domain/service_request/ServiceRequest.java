package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.user.UserId;
import java.time.LocalDateTime;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class ServiceRequest {
  private ServiceRequestId id;
  private UserId userId;
  private ServiceProviderId serviceProviderId;
  private ServiceTypeId serviceTypeId;
  @Nullable private ServiceRequestTitle title;
  @Nullable private ServiceRequestDescription description;
  @Nullable private ServiceRequestLocation location;
  private ServiceRequestStatus status;
  private CreatedAt createdAt;

  private ServiceRequest(
      ServiceRequestId id,
      ServiceRequestParties requestParties,
      ServiceRequestDetails requestDetails,
      ServiceRequestState requestState) {
    this.id = id;
    this.userId = requestParties.userId();
    this.serviceProviderId = requestParties.serviceProviderId();
    this.serviceTypeId = requestParties.serviceTypeId();
    this.title = requestDetails.title();
    this.description = requestDetails.description();
    this.location = requestDetails.location();
    this.status = requestState.status();
    this.createdAt = requestState.createdAt();
  }

  public static ServiceRequest of(ServiceRequestParties selection, ServiceRequestDetails details) {
    return new ServiceRequest(
        ServiceRequestId.generate(),
        selection,
        details,
        new ServiceRequestState(ServiceRequestStatus.PENDING, new CreatedAt(LocalDateTime.now())));
  }
}
