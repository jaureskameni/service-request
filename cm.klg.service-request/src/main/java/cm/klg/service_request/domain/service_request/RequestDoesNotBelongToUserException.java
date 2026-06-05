package cm.klg.service_request.domain.service_request;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_403_002;

import cm.klg.common.base.exception.DomainException;

public class RequestDoesNotBelongToUserException extends DomainException {
  public RequestDoesNotBelongToUserException() {
    super(SERVICE_REQUEST_403_002);
  }
}
