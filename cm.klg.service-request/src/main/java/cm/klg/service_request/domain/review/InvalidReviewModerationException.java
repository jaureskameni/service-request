package cm.klg.service_request.domain.review;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class InvalidReviewModerationException extends RuntimeException {
  public InvalidReviewModerationException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_400_003.value());
  }
}
