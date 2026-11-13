package cm.klg.service_request.adapter.configuration;

import cm.klg.service_request.application.config.Configurations;
import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplicationConfig implements Configurations {
  private final ServiceRequestFollowUpProperties serviceRequestFollowUpProperties;

  @Override
  public Duration getFollowUpDelay() {
    return serviceRequestFollowUpProperties.delay();
  }
}
