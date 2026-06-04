package cm.klg.service_request.domain.service_request;

import org.jspecify.annotations.Nullable;

public record ServiceRequestTitle(String value) {
  public static final @Nullable ServiceRequestTitle NULL = null;

  public static ServiceRequestTitle from(String value) {
    return new ServiceRequestTitle(value);
  }
}
