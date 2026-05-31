package cm.klg.service_request.domain.user;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_404_001;

import cm.klg.common.base.exception.DomainException;

public class UserNotFoundException extends DomainException {
  public UserNotFoundException() {
    super(SERVICE_REQUEST_404_001);
  }
}
