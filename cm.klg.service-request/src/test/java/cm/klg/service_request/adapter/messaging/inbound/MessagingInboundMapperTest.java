package cm.klg.service_request.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamPhoneNumberDTO;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;

class MessagingInboundMapperTest {

  private final MessagingInboundMapper mapper = Mappers.getMapper(MessagingInboundMapper.class);

  @Test
  void shouldMapUserCreatedEventToCommand() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.of(2026, 5, 31, 10, 30);
    UamUserCreatedEventDTO event =
        new UamUserCreatedEventDTO()
            .id(id)
            .lastname("Doe")
            .firstname("John")
            .email("john.doe@example.com")
            .phoneNumber(new UamPhoneNumberDTO().countryCode("237").number("699999999"))
            .createdAt(createdAt);

    var command = mapper.toCreateUserCommand(event);

    assertThat(command.id()).isEqualTo(id);
    assertThat(command.lastname()).isEqualTo("Doe");
    assertThat(command.firstname()).isEqualTo("John");
    assertThat(command.email()).isEqualTo("john.doe@example.com");
    assertThat(command.countryCode()).isEqualTo("237");
    assertThat(command.phoneNumber()).isEqualTo("699999999");
    assertThat(command.createdAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldMapUserCreatedEventWithoutPhoneNumber() {
    UamUserCreatedEventDTO event = new UamUserCreatedEventDTO();

    var command = mapper.toCreateUserCommand(event);

    assertThat(command.countryCode()).isNull();
    assertThat(command.phoneNumber()).isNull();
  }

  @Test
  void shouldMapNullUserCreatedEventToNull() {
    assertThat(mapper.toCreateUserCommand(null)).isNull();
  }

  @Test
  void shouldMapServiceProviderApprovedEventToCommand() {
    UUID userId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();
    LocalDateTime approvedAt = LocalDateTime.of(2026, 5, 31, 11, 0);
    ServiceProviderServiceProviderApprovedEventDTO event =
        new ServiceProviderServiceProviderApprovedEventDTO()
            .userId(userId)
            .serviceProviderId(serviceProviderId)
            .approvedAt(approvedAt);

    var command = mapper.toApproveServiceProviderCommand(event);

    assertThat(command.userId().value()).isEqualTo(userId);
    assertThat(command.serviceProviderId().value()).isEqualTo(serviceProviderId);
    assertThat(command.approvedAt().value()).isEqualTo(approvedAt);
  }

  @Test
  void shouldMapNullServiceProviderApprovedEventToNull() {
    assertThat(mapper.toApproveServiceProviderCommand(null)).isNull();
  }
}
