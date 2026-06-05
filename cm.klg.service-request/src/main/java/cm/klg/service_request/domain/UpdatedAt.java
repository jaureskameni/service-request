package cm.klg.service_request.domain;

import java.time.LocalDateTime;

public record UpdatedAt(LocalDateTime value) {
  public static UpdatedAt from(LocalDateTime value) {
    return new UpdatedAt(value);
  }
}
