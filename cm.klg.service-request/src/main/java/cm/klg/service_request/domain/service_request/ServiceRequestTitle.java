package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestTitle(String value) {
  public static final ServiceRequestTitle NULL = new ServiceRequestTitle("");

  public static ServiceRequestTitle from(@Nullable String value) {
    return value == null ? NULL : new ServiceRequestTitle(value);
  }
}
