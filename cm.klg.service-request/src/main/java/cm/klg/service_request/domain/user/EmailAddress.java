package cm.klg.service_request.domain.user;

import org.jspecify.annotations.Nullable;

public record EmailAddress(@Nullable String value) {
  @Nullable
  public static EmailAddress from(@Nullable String value) {
    return new EmailAddress(value);
  }
}
