package cm.klg.service_request.domain.service_provider;

import java.util.UUID;

public record ServiceProviderId(UUID value) {
  public static final ServiceProviderId NULL =
      new ServiceProviderId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

  public static ServiceProviderId from(UUID value) {
    return new ServiceProviderId(value);
  }

  public static ServiceProviderId generate() {
    return new ServiceProviderId(UUID.randomUUID());
  }

  public boolean isNull() {
    return equals(NULL);
  }
}
