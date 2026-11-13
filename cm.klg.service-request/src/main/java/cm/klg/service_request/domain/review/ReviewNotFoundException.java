package cm.klg.service_request.domain.review;

import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class ReviewNotFoundException extends RuntimeException {
  public ReviewNotFoundException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_404_005.value());
  }
}
