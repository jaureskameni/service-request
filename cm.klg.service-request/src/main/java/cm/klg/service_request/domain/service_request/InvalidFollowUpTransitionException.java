package cm.klg.service_request.domain.service_request;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class InvalidFollowUpTransitionException extends RuntimeException {
  public InvalidFollowUpTransitionException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_409_002.value());
  }
}
