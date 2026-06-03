package cm.klg.service_request.domain.service_request;

import java.util.UUID;

public record ServiceTypeId(UUID value) {
  public static ServiceTypeId from(UUID value) {
    return new ServiceTypeId(value);
  }
}
