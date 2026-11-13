package cm.klg.service_request.adapter.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jk-dev.service-request.follow-up")
public record ServiceRequestFollowUpProperties(Duration delay) {
  public ServiceRequestFollowUpProperties {
    if (delay == null || delay.isZero() || delay.isNegative()) {
      throw new IllegalArgumentException("Service request follow-up delay must be positive");
    }
  }
}
