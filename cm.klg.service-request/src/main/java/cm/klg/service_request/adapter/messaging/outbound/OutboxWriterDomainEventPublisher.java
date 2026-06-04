package cm.klg.service_request.adapter.messaging.outbound;

import static cm.klg.service_request.adapter.messaging.outbound.EventTopics.DESTINATION_SERVICE_REQUEST_OUT;

import cm.klg.generated.service.request.adapter.messaging.outbound.dto.DomainEventType;
import cm.klg.service_request.application.outbound.DomainEventPublisher;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import com.emb.application.outbound.OutboxWriter;
import com.emb.domain.outboxevent.OutboxEventCommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxWriterDomainEventPublisher implements DomainEventPublisher {
  private final OutboxWriter outboxWriter;
  private final OutboxWriterMapper outboxWriterMapper;

  @Override
  public void publishServiceRequestAcceptedEvent(ServiceRequestAcceptedEvent event) {
    outboxWriter.publish(
        new OutboxEventCommand(
            DESTINATION_SERVICE_REQUEST_OUT,
            DomainEventType.SERVICE_REQUEST_ACCEPTED.name(),
            event.id().value().toString(),
            outboxWriterMapper.toServiceRequestAcceptedEventDTO(event)));
  }
}
