package cm.klg.service_request.domain.service_request;

import static cm.klg.service_request.domain.exception.ServiceRequestErrorCode.SERVICE_REQUEST_404_003;

import cm.klg.common.base.exception.DomainException;

public class ServiceRequestNotFoundException extends DomainException {
  public ServiceRequestNotFoundException() {
    super(SERVICE_REQUEST_404_003);
  }
}
