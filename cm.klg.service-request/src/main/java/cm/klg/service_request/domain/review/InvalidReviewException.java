package cm.klg.service_request.domain.review;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class InvalidReviewException extends RuntimeException {
  public InvalidReviewException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_400_002.value());
  }
}
