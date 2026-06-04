package cm.klg.service_request.domain.service_request;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_403_001;

import cm.klg.common.base.exception.DomainException;

public class RequestDoesNotBelongToProviderException extends DomainException {
  public RequestDoesNotBelongToProviderException() {
    super(SERVICE_REQUEST_403_001);
  }
}
