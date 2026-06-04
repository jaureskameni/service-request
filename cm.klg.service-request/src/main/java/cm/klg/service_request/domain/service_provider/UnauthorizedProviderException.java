package cm.klg.service_request.domain.service_provider;

public class UnauthorizedProviderException extends RuntimeException {
  public UnauthorizedProviderException() {
    super("Provider is not authorized for this service request");
  }
}
