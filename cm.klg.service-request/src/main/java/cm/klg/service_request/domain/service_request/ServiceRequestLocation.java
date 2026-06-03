package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestLocation(String value) {
  public static final @Nullable ServiceRequestLocation NULL = null;

  public static ServiceRequestLocation from(String value) {
    return new ServiceRequestLocation(value);
  }
}
