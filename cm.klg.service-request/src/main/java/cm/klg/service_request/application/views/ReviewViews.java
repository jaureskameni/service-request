package cm.klg.service_request.application.views;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface ReviewViews {
  interface PublishedReviewView {
    UUID getId();

    UUID getServiceRequestId();

    UUID getClientId();

    int getRating();

    @Nullable String getComment();

    String getConfirmation();

    LocalDateTime getCreatedAt();
  }
}
