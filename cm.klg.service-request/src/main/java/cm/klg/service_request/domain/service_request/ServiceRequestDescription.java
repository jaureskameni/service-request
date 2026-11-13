package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestDescription(String value) {
  public static final ServiceRequestDescription NULL = new ServiceRequestDescription("");

  public static ServiceRequestDescription from(@Nullable String value) {
    return value == null ? NULL : new ServiceRequestDescription(value);
  }
}
