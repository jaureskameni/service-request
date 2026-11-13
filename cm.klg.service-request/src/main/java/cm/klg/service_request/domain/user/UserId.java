package cm.klg.service_request.domain.user;

import java.util.UUID;

public record UserId(UUID value) {
  public static final UserId NULL =
      new UserId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

  public static UserId from(UUID value) {
    return new UserId(value);
  }
}
