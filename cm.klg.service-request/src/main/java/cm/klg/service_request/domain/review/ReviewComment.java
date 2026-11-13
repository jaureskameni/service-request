package cm.klg.service_request.domain.review;

import org.jspecify.annotations.Nullable;

public record ReviewComment(String value) {
  public static final ReviewComment NULL = new ReviewComment("");

  public static ReviewComment from(@Nullable String value) {
    return value == null ? NULL : new ReviewComment(value);
  }
}
