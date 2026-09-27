package cm.klg.service_request.domain.common;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_400_001;

import cm.klg.common.base.exception.DomainException;

public class InvalidPaginationDataException extends DomainException {
  public InvalidPaginationDataException() {
    super(SERVICE_REQUEST_400_001);
  }
}
