package cm.klg.service_request.adapter.messaging.inbound;

import com.emb.application.inbound.EventReception;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class InboundEventReception {
  private final EventReception eventReception;

  @Bean
  public Consumer<Message<byte[]>> user() {
    return eventReception::receive;
  }
}
