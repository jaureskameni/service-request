package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.event.ServiceRequestCreatedEvent;
import cm.klg.service_request.domain.event.ServiceRequestRejectedEvent;

public interface DomainEventPublisher {
  void publishServiceRequestCreatedEvent(ServiceRequestCreatedEvent event);

  void publishServiceRequestAcceptedEvent(ServiceRequestAcceptedEvent event);

  void publishServiceRequestRejectedEvent(ServiceRequestRejectedEvent event);
}
