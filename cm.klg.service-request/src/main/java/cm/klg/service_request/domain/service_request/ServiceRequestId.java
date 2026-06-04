package cm.klg.service_request.domain.service_request;

import java.util.UUID;

public record ServiceRequestId(UUID value) {
  public static ServiceRequestId generate() {
    return new ServiceRequestId(UUID.randomUUID());
  }

  public static ServiceRequestId from(UUID value) {
    return new ServiceRequestId(value);
  }
}
