package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestLocation(String value) {
  public static final ServiceRequestLocation NULL = new ServiceRequestLocation("");

  public static ServiceRequestLocation from(@Nullable String value) {
    return value == null ? NULL : new ServiceRequestLocation(value);
  }
}
