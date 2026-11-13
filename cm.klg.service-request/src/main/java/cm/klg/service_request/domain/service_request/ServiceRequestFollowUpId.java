package cm.klg.service_request.domain.service_request;

import java.util.UUID;

public record ServiceRequestFollowUpId(UUID value) {
  public static final ServiceRequestFollowUpId NULL =
      new ServiceRequestFollowUpId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

  public static ServiceRequestFollowUpId generate() {
    return new ServiceRequestFollowUpId(UUID.randomUUID());
  }

  public static ServiceRequestFollowUpId from(UUID value) {
    return new ServiceRequestFollowUpId(value);
  }
}
