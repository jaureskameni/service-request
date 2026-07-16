package cm.klg.service_request.domain.service_provider;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_403_003;

import cm.klg.common.base.exception.DomainException;

public class UnauthorizedProviderException extends DomainException {
  public UnauthorizedProviderException() {
    super(SERVICE_REQUEST_403_003);
  }
}
