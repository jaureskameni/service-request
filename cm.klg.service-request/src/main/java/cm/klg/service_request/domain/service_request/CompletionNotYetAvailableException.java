package cm.klg.service_request.domain.service_request;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class CompletionNotYetAvailableException extends RuntimeException {
  public CompletionNotYetAvailableException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_409_003.value());
  }
}
