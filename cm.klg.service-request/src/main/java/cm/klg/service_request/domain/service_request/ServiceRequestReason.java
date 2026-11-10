package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestReason(String value) {
  public static final ServiceRequestReason NULL = new ServiceRequestReason("");

  public static ServiceRequestReason from(@Nullable String value) {
    return value == null ? NULL : new ServiceRequestReason(value);
  }
}
