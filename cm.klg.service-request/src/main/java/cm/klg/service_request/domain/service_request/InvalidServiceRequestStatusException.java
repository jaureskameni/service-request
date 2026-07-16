package cm.klg.service_request.domain.service_request;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_409_001;

import cm.klg.common.base.exception.DomainException;

public class InvalidServiceRequestStatusException extends DomainException {
  public InvalidServiceRequestStatusException() {
    super(SERVICE_REQUEST_409_001);
  }
}
