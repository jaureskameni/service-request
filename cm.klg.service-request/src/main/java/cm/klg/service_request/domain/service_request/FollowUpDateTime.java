package cm.klg.service_request.domain.service_request;

import java.time.LocalDateTime;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/** Date/time value object with an explicit sentinel for optional domain values. */
public record FollowUpDateTime(@Nullable LocalDateTime value) {
  public static final FollowUpDateTime NULL = new FollowUpDateTime(null);

  public static FollowUpDateTime from(@Nullable LocalDateTime value) {
    return value == null ? NULL : new FollowUpDateTime(value);
  }

  public boolean isNull() {
    return this == NULL || value == null;
  }

  public LocalDateTime valueOrThrow() {
    return Objects.requireNonNull(value, "Follow-up date/time is absent");
  }
}
