package cm.klg.service_request.application.outbound;

import cm.klg.service_request.application.views.ServiceRequestViews.ServiceRequestView1;
import cm.klg.service_request.domain.service_provider.ServiceProviderId;
import cm.klg.service_request.domain.service_request.ServiceRequest;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import cm.klg.service_request.domain.service_request.ServiceRequestStatus;
import cm.klg.service_request.domain.user.UserId;
import cm.klg.service_request.utils.PageData;
import cm.klg.service_request.utils.PaginationFetchRequest;

public interface ServiceRequestRepository {
  void insert(ServiceRequest serviceRequest);

  PageData<ServiceRequestView1> loadAllMyRequestsAsView1(
      UserId userId, PaginationFetchRequest pagination);

  PageData<ServiceRequestView1> loadAllMyRequestByStatusAsView1(
      UserId userId, ServiceRequestStatus status, PaginationFetchRequest pagination);

  PageData<ServiceRequestView1> loadAllRequestsByProviderAsView1(
      ServiceProviderId providerId, PaginationFetchRequest pagination);

  PageData<ServiceRequestView1> loadAllRequestByProviderAndStatusAsView1(
      ServiceProviderId providerId, ServiceRequestStatus status, PaginationFetchRequest pagination);

  ServiceRequestView1 loadByIdAsView1(ServiceRequestId id);
}
