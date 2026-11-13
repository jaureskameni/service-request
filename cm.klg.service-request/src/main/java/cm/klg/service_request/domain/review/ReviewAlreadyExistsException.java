package cm.klg.service_request.domain.review;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class ReviewAlreadyExistsException extends RuntimeException {
  public ReviewAlreadyExistsException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_409_005.value());
  }
}
