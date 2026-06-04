package cm.klg.service_request.adapter.messaging;

import cm.klg.service_request.adapter.messaging.outbound.OutboxWriterDomainEventPublisher;
import cm.klg.service_request.adapter.messaging.outbound.OutboxWriterMapper;
import cm.klg.service_request.application.outbound.DomainEventPublisher;
import com.emb.application.outbound.OutboxWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSpringBeans {

  @Bean
  public DomainEventPublisher domainEventPublisher(
      OutboxWriter outboxWriter, OutboxWriterMapper outboxWriterMapper) {
    return new OutboxWriterDomainEventPublisher(outboxWriter, outboxWriterMapper);
  }
}
