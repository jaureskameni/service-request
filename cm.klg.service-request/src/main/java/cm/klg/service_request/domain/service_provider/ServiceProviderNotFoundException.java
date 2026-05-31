package cm.klg.service_request.domain.service_provider;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_404_002;

import cm.klg.common.base.exception.DomainException;

public class ServiceProviderNotFoundException extends DomainException {
  public ServiceProviderNotFoundException() {
    super(SERVICE_REQUEST_404_002);
  }
}
