package cm.klg.service_request.domain.service_request;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class ServiceRequestFollowUpNotFoundException extends RuntimeException {
  public ServiceRequestFollowUpNotFoundException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_404_004.value());
  }
}
