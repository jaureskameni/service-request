package cm.klg.service_request.adapter.messaging.outbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.service.request.adapter.messaging.outbound.dto.ServiceRequestAcceptedEventDTO;
import cm.klg.service_request.domain.event.ServiceRequestAcceptedEvent;
import cm.klg.service_request.domain.service_request.ServiceRequestId;
import com.emb.application.outbound.OutboxWriter;
import com.emb.domain.outboxevent.OutboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutboxWriterDomainEventPublisherTest {

  @Mock private OutboxWriter outboxWriter;
  @Mock private OutboxWriterMapper outboxWriterMapper;

  @InjectMocks private OutboxWriterDomainEventPublisher publisher;

  @Test
  void shouldPublishServiceRequestAcceptedEvent() {
    // Given
    UUID requestId = UUID.randomUUID();
    var event = mock(ServiceRequestAcceptedEvent.class);
    var dto = mock(ServiceRequestAcceptedEventDTO.class);
    when(event.id()).thenReturn(new ServiceRequestId(requestId));
    when(outboxWriterMapper.toServiceRequestAcceptedEventDTO(event)).thenReturn(dto);

    // When
    publisher.publishServiceRequestAcceptedEvent(event);

    // Then
    ArgumentCaptor<OutboxEventCommand> captor = ArgumentCaptor.forClass(OutboxEventCommand.class);
    verify(outboxWriter).publish(captor.capture());
    OutboxEventCommand captured = captor.getValue();
    assertThat(captured).isNotNull();
  }
}
