package cm.klg.service_request.adapter.messaging;

import cm.klg.service_request.adapter.messaging.outbound.OutboxWriterDomainEventPublisher;
import cm.klg.service_request.adapter.messaging.outbound.OutboxWriterMapper;
import cm.klg.service_request.application.outbound.DomainEventPublisher;
import com.emb.application.outbound.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSpringBeans {

  @Bean
  public DomainEventPublisher domainEventPublisher(
      EventPublisher eventPublisher, OutboxWriterMapper outboxWriterMapper) {
    return new OutboxWriterDomainEventPublisher(eventPublisher, outboxWriterMapper);
  }
}
