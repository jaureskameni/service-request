package cm.klg.service_request.domain.review;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class ReviewNotEligibleException extends RuntimeException {
  public ReviewNotEligibleException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_409_004.value());
  }
}
