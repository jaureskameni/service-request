package cm.klg.service_request.adapter.messaging.outbound;

import static cm.klg.service_request.adapter.messaging.outbound.EventTopics.DESTINATION_SERVICE_REQUEST_OUT;

import cm.klg.generated.service.request.adapter.messaging.outbound.dto.DomainEventType;
import cm.klg.service_request.application.outbound.DomainEventPublisher;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.event.ServiceRequestCancelledEvent;
import cm.klg.service_request.domain.event.ServiceRequestCreatedEvent;
import cm.klg.service_request.domain.event.ServiceRequestRejectedEvent;
import com.emb.application.outbound.EventPublisher;
import com.emb.domain.outboxevent.EventCommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutboxWriterDomainEventPublisher implements DomainEventPublisher {
  private final EventPublisher eventPublisher;
  private final OutboxWriterMapper outboxWriterMapper;

  @Override
  public void publishServiceRequestCreatedEvent(ServiceRequestCreatedEvent event) {
    this.publish(
        DomainEventType.SERVICE_REQUEST_CREATED,
        event.id().value().toString(),
        outboxWriterMapper.toServiceRequestCreatedEventDTO(event));
  }

  @Override
  public void publishServiceRequestAcceptedEvent(ServiceRequestAcceptedEvent event) {
    this.publish(
        DomainEventType.SERVICE_REQUEST_ACCEPTED,
        event.id().value().toString(),
        outboxWriterMapper.toServiceRequestAcceptedEventDTO(event));
  }

  @Override
  public void publishServiceRequestRejectedEvent(ServiceRequestRejectedEvent event) {
    this.publish(
        DomainEventType.SERVICE_REQUEST_REJECTED,
        event.id().value().toString(),
        outboxWriterMapper.toServiceRequestRejectedEventDTO(event));
  }

  @Override
  public void publishServiceRequestCancelledEvent(ServiceRequestCancelledEvent event) {
    this.publish(
        DomainEventType.SERVICE_REQUEST_CANCELLED,
        event.id().value().toString(),
        outboxWriterMapper.toServiceRequestCancelledEventDTO(event));
  }

  private void publish(DomainEventType eventType, String key, Object payload) {
    eventPublisher.publish(
        new EventCommand(DESTINATION_SERVICE_REQUEST_OUT, eventType.name(), key, payload));
  }
}
