package cm.klg.service_request.application.outbound;

import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;

public interface DomainEventPublisher {
  void publishServiceRequestAcceptedEvent(ServiceRequestAcceptedEvent event);
}
