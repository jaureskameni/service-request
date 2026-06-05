package cm.klg.service_request.application.views;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface ServiceRequestViews {
  interface ServiceRequestView1 {
    UUID getId();

    UUID getUserId();

    UUID getServiceProviderId();

    UUID getServiceTypeId();

    @Nullable String getTitle();

    @Nullable String getDescription();

    @Nullable String getLocation();

    String getStatus();

    @Nullable String getReason();

    LocalDateTime getCreatedAt();

    @Nullable LocalDateTime getUpdatedAt();
  }
}
