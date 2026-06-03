package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestDescription(String value) {
  public static final @Nullable ServiceRequestDescription NULL = null;

  public static ServiceRequestDescription from(String value) {
    return new ServiceRequestDescription(value);
  }
}
