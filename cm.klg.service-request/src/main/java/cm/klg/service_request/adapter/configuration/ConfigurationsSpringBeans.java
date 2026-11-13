package cm.klg.service_request.adapter.configuration;

import cm.klg.service_request.application.config.Configurations;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(ServiceRequestFollowUpProperties.class)
public class ConfigurationsSpringBeans {
  @Bean
  public Configurations configurations(
      ServiceRequestFollowUpProperties serviceRequestFollowUpProperties) {
    return new ApplicationConfig(serviceRequestFollowUpProperties);
  }
}
