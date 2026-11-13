package cm.klg.service_request.domain.review;

import java.util.UUID;

public record ReviewId(UUID value) {
  public static ReviewId generate() {
    return new ReviewId(UUID.randomUUID());
  }

  public static ReviewId from(UUID value) {
    return new ReviewId(value);
  }
}
