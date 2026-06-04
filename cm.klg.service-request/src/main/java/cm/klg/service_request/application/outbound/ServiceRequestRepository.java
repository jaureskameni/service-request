package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.service_request.ServiceRequest;

public interface ServiceRequestRepository {
  void insert(ServiceRequest serviceRequest);
}
