package cm.klg.service_request.domain.service_request;

import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

public record AcceptedAt(@Nullable LocalDateTime value) {
  public static final AcceptedAt NULL = new AcceptedAt(null);

  public static AcceptedAt from(@Nullable LocalDateTime value) {
    return value == null ? NULL : new AcceptedAt(value);
  }
}
