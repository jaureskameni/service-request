package cm.klg.service_request.domain.service_request;

import cm.klg.common.base.exception.DomainException;
import cm.klg.service_request.domain.exception.ServiceRequestErrorCode;

public class UserIsNotInvolvedException extends DomainException {
  public UserIsNotInvolvedException() {
    super(ServiceRequestErrorCode.SERVICE_REQUEST_403_004);
  }
}
