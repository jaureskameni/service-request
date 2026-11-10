package cm.klg.service_request.domain;

import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

public record UpdatedAt(@Nullable LocalDateTime value) {
  public static final UpdatedAt NULL = new UpdatedAt(null);

  public static UpdatedAt from(@Nullable LocalDateTime value) {
    return value == null ? NULL : new UpdatedAt(value);
  }
}
