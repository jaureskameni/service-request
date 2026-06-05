package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestReason(String value) {
  public static final @Nullable ServiceRequestReason NULL = null;

  public static ServiceRequestReason from(String value) {
    return new ServiceRequestReason(value);
  }
}
